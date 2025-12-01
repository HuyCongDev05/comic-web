from fastapi import APIRouter, BackgroundTasks, Query, WebSocket, WebSocketDisconnect
from fastapi.encoders import jsonable_encoder
import math
from typing import Optional, Set

from app.core.db import get_redis_connection, mongo_collection
from app.core.response import api_response
from app.services.comic_service import crawl_comic
from app.services.list_crawler import crawl_all

router = APIRouter(prefix="/api/v1/dashboard")

checkCrawl = True
active_connections: Set[WebSocket] = set()


@router.get("/crawl")
def api_crawl_all(background_tasks: BackgroundTasks):
    global checkCrawl

    if checkCrawl:
        checkCrawl = False
        background_tasks.add_task(crawl_all)

        return api_response(
            success=True,
            message="request successfully",
            data={},
            status=200
        )
    else:
        return api_response(
            success=False,
            message="request processing",
            data={},
            status=409
        )

@router.get("/crawl/{originName}")
def api_crawl_comic(originName: str, background_tasks: BackgroundTasks):
    global checkCrawl

    if checkCrawl:
        checkCrawl = False
        background_tasks.add_task(crawl_comic, originName)

        return api_response(
            success=True,
            message="request successfully",
            data={},
            status=200
        )
    else:
        return api_response(
            success=False,
            message="request processing",
            data={},
            status=409
        )

@router.get("/crawl-last-time")
def api_crawl_last_time():
    value = get_redis_connection().get("crawl-last-time")

    return api_response(
        success=True,
        message="request successfully",
        data={
            "crawl_last_time": value
        },
        status=200
    )


@router.get("/crawl-history")
def api_crawl_history(
        page: int = Query(1, ge=1)
):
    PAGE_SIZE = 8
    start_index = (page - 1) * PAGE_SIZE
    stop_index = start_index + PAGE_SIZE - 1
    r = get_redis_connection()

    history_items = r.lrange("crawl-history", start_index, stop_index)

    total_count = r.llen("crawl-history")

    total_pages = (total_count + PAGE_SIZE - 1) // PAGE_SIZE

    return api_response(
        success=True,
        message="request successfully",
        data={
            "content": history_items,
            "currentPageSize": len(history_items),
            "totalElements": total_count,
            "totalPages": total_pages
        },
        status=200
    )


@router.get("/crawl-error")
def api_crawl_error(page: int = Query(1, ge=1)):
    PAGE_SIZE = 8
    skip_count = (page - 1) * PAGE_SIZE

    cursor = mongo_collection.find({}) \
        .sort("_id", -1) \
        .skip(skip_count) \
        .limit(PAGE_SIZE)

    raw_logs = list(cursor)

    error_logs = jsonable_encoder(raw_logs)

    total_count = mongo_collection.count_documents({})

    total_pages = math.ceil(total_count / PAGE_SIZE)

    return api_response(
        success=True,
        message="request successfully",
        data={
            "content": error_logs,
            "totalElements": total_count,
            "totalPages": total_pages
        },
        status=200
    )