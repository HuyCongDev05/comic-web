import datetime
from redis.exceptions import RedisError
from app.core.db import get_redis_connection

def add_daily_history(comic_id, comic_name, key="crawl-history"):
    r = get_redis_connection()

    try:
        timestamp = datetime.datetime.now().isoformat()
        entry = f"{comic_id},{comic_name},{timestamp}"

        existing_items = r.lrange(key, 0, -1)
        for item in existing_items:
            decoded = item.decode()
            if decoded.startswith(f"{comic_id},") or decoded.split(',')[1] == comic_name:
                r.lrem(key, 0, decoded)

        r.lpush(key, entry)

        if r.ttl(key) == -1:
            now = datetime.datetime.now()
            tomorrow = datetime.datetime.combine(now.date() + datetime.timedelta(days=1), datetime.time.min)
            seconds_until_midnight = int((tomorrow - now).total_seconds())
            r.expire(key, seconds_until_midnight)

    except RedisError as e:
        print(f"⚠️ Lỗi khi thao tác với Redis: {e}")