import time

import requests

from app.core.config import HEADERS
from app.core.logs import log_error_chapter, log_error_unspecified


def fetch_json(url: str, origin_name: str | None = None):
    for _ in range(3):
        try:
            res = requests.get(url, headers=HEADERS, timeout=12)
            if res.status_code == 200:
                return res.json()
            elif res.status_code == 429:
                print("⚠️ Quá nhiều request — chờ 5s")
                time.sleep(5)
        except Exception as e:
            print(f"⚠️ Lỗi khi tải {url}: {e}")
            time.sleep(2)

    if origin_name:
        log_error_chapter(origin_name, url)
    else:
        log_error_unspecified("Lỗi không xác định", origin_name, url)
    return None
