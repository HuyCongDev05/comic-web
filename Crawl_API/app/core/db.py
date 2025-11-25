import time

import redis
from mysql.connector import pooling
from pymongo import MongoClient, ReturnDocument
from redis.exceptions import ConnectionError as RedisConnectionError

from app.core.config import (
    DB_CONFIG,
    MONGO_URI,
    MONGO_DB,
    MONGO_COLLECTION,
    REDIS_HOST,
    REDIS_PORT
)

# MySQL
db_pool = pooling.MySQLConnectionPool(
    pool_name="mypool",
    pool_size=10,
    pool_reset_session=True,
    **DB_CONFIG,
)


def get_connection():
    for _ in range(3):
        try:
            return db_pool.get_connection()
        except Exception as e:
            print(f"⚠️ Không thể kết nối DB: {e}, thử lại…")
            time.sleep(2)
    raise RuntimeError("❌ Kết nối DB thất bại sau nhiều lần thử.")


# MongoDB
mongo_client = MongoClient(MONGO_URI)
mongo_db = mongo_client[MONGO_DB]
mongo_collection = mongo_db[MONGO_COLLECTION]


def get_next_log_id():
    counter = mongo_db["counters"].find_one_and_update(
        {"_id": f"{MONGO_COLLECTION}_id"},
        {"$inc": {"seq": 1}},
        return_document=ReturnDocument.AFTER,
        upsert=True
    )
    return counter["seq"]


# Redis
redis_pool = redis.ConnectionPool(
    host=REDIS_HOST,
    port=REDIS_PORT,
    db=0,
    decode_responses=True,
    max_connections=20,
    socket_connect_timeout=5,
    socket_keepalive=True,
)


def get_redis_connection():
    for attempt in range(3):
        try:
            r = redis.Redis(connection_pool=redis_pool)
            r.ping()
            return r
        except RedisConnectionError as e:
            print(f"⚠️ Không thể kết nối Redis (lần {attempt + 1}): {e}")
            time.sleep(2)
    raise RuntimeError("❌ Kết nối Redis thất bại sau nhiều lần thử.")
