import math

from fastapi import APIRouter, Query, WebSocket, WebSocketDisconnect
from fastapi.encoders import jsonable_encoder

from app.api.socket import active_connections
from app.core.db import get_redis_connection, mongo_collection
from app.core.response import api_response
from app.services.crawl_by_originName import crawl_comic_by_originName
from app.services.list_crawler import crawl_all

router = APIRouter(prefix="/api/v1/dashboard")

checkCrawl = True

import asyncio

@router.get("/crawl")
async def api_crawl_all():
    global checkCrawl

    if checkCrawl:
        checkCrawl = False

        asyncio.create_task(crawl_all())

        return api_response(
            success=True,
            message="request successfully",
            data={"websocket_url": "/api/v1/dashboard/ws/crawl"},
            status=200
        )
    else:
        return api_response(
            success=False,
            message="request processing",
            data={},
            status=409
        )


@router.websocket("/ws/crawl")
async def crawl_ws(ws: WebSocket):
    await ws.accept()
    active_connections.add(ws)

    try:
        while True:
            await ws.receive_text()
    except WebSocketDisconnect:
        active_connections.discard(ws)


@router.get("/crawl/{originName}")
async def api_crawl_comic(originName: str):
    global checkCrawl

    if checkCrawl:
        checkCrawl = False
        asyncio.create_task(crawl_comic_by_originName(originName))

        return api_response(
            success=True,
            message="request successfully",
            data={"websocket_url": "/api/v1/dashboard/ws/crawl"},
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