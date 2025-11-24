from fastapi import APIRouter, BackgroundTasks

from app.core.response import api_response
from app.services.comic_service import crawl_comic
from app.services.list_crawler import crawl_all

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