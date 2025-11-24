import threading
import uuid
from datetime import datetime

from app.core.db import get_connection
from app.core.logs import log_error_chapter
from app.utils.http import fetch_json
from app.utils.text import parse_chapter_number

lock = threading.Lock()


def chapter_exists_in_comic(comic_id: int, chap_num: float | None) -> bool:
    if chap_num is None:
        return False

    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT chapter FROM chapter WHERE comic_id=%s", (comic_id,))
    rows = cursor.fetchall()
    cursor.close()
    conn.close()

    for r in rows:
        try:
            if float(r["chapter"]) == float(chap_num):
                return True
        except Exception:
            continue
    return False


def crawl_chapter_images(api_url: str, uuid_chapter: str, conn, origin_name: str):
    cursor = conn.cursor()
    data = fetch_json(api_url, origin_name)
    if not data or data.get("status") != "success":
        log_error_chapter(origin_name, api_url)
        cursor.close()
        return

    item = data["data"]["item"]
    path = item.get("chapter_path", "").rstrip("/")
    images = item.get("chapter_image", [])
    if not images:
        log_error_chapter(origin_name, api_url)
        cursor.close()
        return

    values = [
        (f"{path}/{img['image_file']}".lstrip("/"), uuid_chapter, img["image_page"])
        for img in images
    ]

    try:
        cursor.executemany(
            """
            INSERT
            IGNORE INTO image_chapter (image_link, uuid_chapter, image_number)
            VALUES (
            %s,
            %s,
            %s
            )
            """,
            values,
        )
        conn.commit()
        print(f"🖼️ Đã thêm {len(values)} ảnh cho chapter {uuid_chapter}")
    except Exception:
        conn.rollback()
        log_error_chapter(origin_name, api_url)
    finally:
        cursor.close()


def process_chapter(chap: dict, comic_id: int, origin_name: str):
    api_chapter = chap.get("chapter_api_data")
    chap_name = chap.get("chapter_name")
    chap_num = parse_chapter_number(chap_name)
    now = datetime.now()

    if chapter_exists_in_comic(comic_id, chap_num):
        return

    conn = get_connection()
    cursor = conn.cursor(dictionary=True)

    with lock:
        uuid_chapter = str(uuid.uuid4())
        try:
            cursor.execute(
                """
                INSERT INTO chapter (uuid_chapter, comic_id, chapter, time)
                VALUES (%s, %s, %s, %s)
                """,
                (uuid_chapter, comic_id, chap_num, now),
            )
            conn.commit()
            print(f"📘 Đã thêm chapter {chap_name}")
        except Exception:
            conn.rollback()
            log_error_chapter(origin_name, "")
            conn.close()
            return

    crawl_chapter_images(api_chapter, uuid_chapter, conn, origin_name)
    conn.close()
