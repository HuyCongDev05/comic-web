from datetime import datetime

from app.core.db import mongo_collection, get_next_log_id


def log_error_chapter(origin_name: str, url: str):
    try:
        mongo_collection.insert_one({
            "_id": get_next_log_id(),
            "type": "chapters",
            "originName": origin_name,
            "url": url,
            "messages": "Lỗi crawl chapters",
            "created_at": datetime.utcnow(),
        })
    except Exception as e:
        print(f"⚠️ Không thể ghi log chapter vào Mongo: {e}")


def log_error_general(message: str, error_type: str, origin: str) -> None:
    try:
        mongo_collection.insert_one({
            "_id": get_next_log_id(),
            "type": error_type,
            "originName": origin,
            "messages": message.strip(),
            "created_at": datetime.utcnow(),
        })
    except Exception as e:
        print(f"⚠️ Không thể ghi log vào Mongo: {e}")


def log_error_unspecified(message: str, origin: str, url: str) -> None:
    try:
        mongo_collection.insert_one({
            "_id": get_next_log_id(),
            "type": "unspecified",
            "originName": origin or "",
            "url": url,
            "messages": message.strip(),
            "created_at": datetime.utcnow(),
        })
    except Exception as e:
        print(f"⚠️ Không thể ghi log vào Mongo: {e}")
