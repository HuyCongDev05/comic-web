from concurrent.futures import ThreadPoolExecutor, as_completed
from datetime import datetime

import app.api.routes as routes
from app.core.config import API_BASE
from app.core.db import get_connection, get_redis_connection
from app.core.logs import log_error_general
from app.utils.http import fetch_json
from app.utils.text import clean_intro_text, parse_chapter_number
from .chapter_service import process_chapter
from app.core.history import add_daily_history


def crawl_comic(slug: str, image_from_list: str | None = None) -> bool:
    print(f"\n🚀 Bắt đầu crawl truyện: {slug}")
    api_url = f"{API_BASE}{slug}"
    data = fetch_json(api_url, slug)
    if not data:
        log_error_general(
            message="Không lấy được dữ liệu truyện",
            error_type="data",
            origin=slug
        )
        return False

    item = data.get("data", {}).get("item", {})
    if not item:
        log_error_general(message="Dữ liệu truyện trống",
                          error_type="item",
                          origin=slug
                          )
        return False

    updatedAt = item.get("updatedAt")
    try:
        if updatedAt:
            time = get_redis_connection().get("crawl-last-time")

            if updatedAt < time:
                msg = (
                    f"🛑 Truyện {slug} là truyện của ngày cũ "
                    f"({updatedAt} < {time}) — dừng crawl."
                )
                print(msg)
                routes.checkCrawl = True
                return False
    except Exception as e:
        print(f"⚠️ Không thể so sánh updatedAt cho {slug}: {e}")
        routes.checkCrawl = True
        routes.checkCrawlBySlug = True
        return False

    raw_intro = item.get("content", "") or ""
    clean_intro = clean_intro_text(raw_intro)
    comic_name = item.get("name") or item.get("title") or slug

    # Ảnh
    if image_from_list:
        image_link = image_from_list.strip()
    else:
        thumb = item.get("thumb_url", "") or ""
        image_link = f"/uploads/comics/{thumb}" if thumb else ""

    # Trạng thái
    status_raw = (item.get("status") or "").lower()
    if "ongoing" in status_raw or "updating" in status_raw:
        status = "đang cập nhật"
    elif "complete" in status_raw or "full" in status_raw:
        status = "đã hoàn thành"
    else:
        status = "đang cập nhật"

    # Danh mục
    categories = item.get("category", [])
    category_slugs = [c.get("slug") for c in categories if c.get("slug")]

    # Chapters
    chapters_group = item.get("chapters", [])
    chapters = chapters_group[0]["server_data"] if chapters_group else []
    last_chap_name = chapters[-1]["chapter_name"] if chapters else None
    last_chap_num = parse_chapter_number(last_chap_name)

    # --- Lưu vào DB ---
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT id FROM comic WHERE origin_name=%s", (slug,))
    row = cursor.fetchone()

    if not row:
        # Thêm mới comic
        import uuid

        try:
            uuid_comic = str(uuid.uuid4())
            cursor.execute(
                """
                INSERT INTO comic (uuid_comic, origin_name, name, image_link, intro, last_chapter,
                                   status, update_time)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
                """,
                (
                    uuid_comic,
                    slug,
                    comic_name,
                    image_link,
                    clean_intro,
                    last_chap_num,
                    status,
                    datetime.now(),
                ),
            )
            conn.commit()
            comic_id = cursor.lastrowid
            print(f"✅ Đã thêm mới comic: {comic_name} (id={comic_id})")

            cursor.execute(
                """
                INSERT
                IGNORE INTO comic_stats (comic_id, avg_rating, total_rating)
                VALUES (
                %s,
                %s,
                %s
                )
                """,
                (comic_id, 0, 0),
            )
            conn.commit()
        except Exception:
            conn.rollback()
            log_error_general(
                message="Không crawl được truyện",
                error_type="comic",
                origin=slug
            )
            cursor.close()
            conn.close()
            return False
    else:
        # Update comic
        comic_id = row["id"]
        try:
            cursor.execute(
                """
                UPDATE comic
                SET intro=%s,
                    last_chapter=%s,
                    update_time=%s,
                    name=%s,
                    image_link=%s
                WHERE id = %s
                """,
                (
                    clean_intro,
                    last_chap_num,
                    datetime.now(),
                    comic_name,
                    image_link,
                    comic_id,
                ),
            )
            conn.commit()
            print(f"✅ Đã cập nhật truyện: {slug}")
        except Exception:
            conn.rollback()
            log_error_general(
                message="Không cập nhật được dữ liệu truyện",
                error_type="comic",
                origin=slug
            )

    # --- Thể loại ---
    for c_slug in category_slugs:
        cursor.execute("SELECT id FROM categories WHERE origin_name=%s", (c_slug,))
        cat_row = cursor.fetchone()
        if not cat_row:
            try:
                cursor.execute(
                    """
                    INSERT INTO categories (name, origin_name, detail)
                    VALUES (%s, %s, %s)
                    """,
                    (c_slug.capitalize(), c_slug, "Đang cập nhật"),
                )
                conn.commit()
                cursor.execute(
                    "SELECT id FROM categories WHERE origin_name=%s", (c_slug,)
                )
                cat_row = cursor.fetchone()
            except Exception:
                conn.rollback()
                log_error_general(
                    message="Không cập nhật được thể loại",
                    error_type="categories",
                    origin=slug
                )
                continue

        category_id = cat_row["id"]

        cursor.execute(
            """
            SELECT 1
            FROM comic_categories
            WHERE comic_id = %s
              AND categories_id = %s
            """,
            (comic_id, category_id),
        )
        if not cursor.fetchone():
            try:
                cursor.execute(
                    """
                    INSERT INTO comic_categories (comic_id, categories_id)
                    VALUES (%s, %s)
                    """,
                    (comic_id, category_id),
                )
                conn.commit()
            except Exception:
                conn.rollback()
                log_error_general(
                    message="Không cập nhật được thể loại",
                    error_type="categories",
                    origin=slug
                )

    cursor.close()
    conn.close()

    # --- Chapter ---
    if chapters:
        with ThreadPoolExecutor(max_workers=5) as executor:
            futures = [
                executor.submit(process_chapter, c, comic_id, slug) for c in chapters
            ]
            for f in as_completed(futures):
                try:
                    f.result()
                except Exception:
                    log_error_general(
                        message="Không thể crawl được chapter",
                        error_type="chapters",
                        origin=slug
                    )

    print(f"🎉 Hoàn tất crawl truyện có originName: {slug})")
    add_daily_history(comic_name)
    return True
