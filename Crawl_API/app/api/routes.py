from fastapi import APIRouter, BackgroundTasks

from app.core.response import api_response
from app.services.comic_service import crawl_comic
from app.services.list_crawler import crawl_all
from app.core.db import get_redis_connection

router = APIRouter(prefix="/api/v1/dashboard")

checkCrawl = True


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
def api_crawl_history():
    value = get_redis_connection().get("crawl-history")
    return api_response(
        success=True,
        message="request successfully",
        data={
            "crawl_history": value
        },
        status=200
    )