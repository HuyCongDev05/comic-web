import time
from datetime import datetime

import requests

import app.api.routes as routes
from app.core.config import LIST_API_BASE, HEADERS
from app.core.db import get_redis_connection
from .comic_service import crawl_comic
from app.api.routes import broadcast


def fetch_page_data(page: int):
    url = f"{LIST_API_BASE}?page={page}"
    for _ in range(3):
        try:
            res = requests.get(url, headers=HEADERS, timeout=15)
            if res.status_code == 429:
                time.sleep(8)
                continue
            data = res.json()
            if data.get("status") == "success":
                return data
        except Exception:
            time.sleep(3)
    return None


def crawl_all():
    page = 1
    while True:
        print(f"\n🌐 Đang crawl trang {page}")
        data = fetch_page_data(page)
        if not data:
            print(f"🛑 Hết dữ liệu hoặc lỗi ở trang {page}. Dừng lại.")
            routes.checkCrawl = True
            broadcast({
            "success":"True",
            "message":"request successfully",
            "data":"{}",
            "status":"200"
            })
            break

        items = data["data"].get("items", [])
        images = data["data"]["seoOnPage"].get("og_image", [])
        if not items:
            print("🛑 Không còn truyện nào — dừng crawl.")
            routes.checkCrawl = True
            break

        for i, item in enumerate(items):
            slug = item.get("slug")
            image_for_item = images[i] if i < len(images) else None

            ok = crawl_comic(slug, image_from_list=image_for_item)
            if not ok:
                print(f"⛔ Gặp truyện cũ hoặc lỗi tại {slug} — dừng toàn bộ crawl_all.")
                routes.checkCrawl = True
                timestamp = datetime.utcnow().strftime("%Y-%m-%dT%H:%M:%S.%f")[:-3] + "Z"
                get_redis_connection().set("crawl-last-time", timestamp)
                return

            time.sleep(0.2)

        page += 1

    print(f"\n✅ Hoàn tất crawl toàn bộ.")
    routes.checkCrawl = True