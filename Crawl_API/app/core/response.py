from datetime import datetime

from fastapi.responses import JSONResponse


def api_response(success: bool, message: str, data=None, status: int = 200):
    return JSONResponse(
        content={
            "success": success,
            "status": status,
            "message": message,
            "data": data or {},
            "timestamp": datetime.utcnow().isoformat() + "Z"
        },
        status_code=status
    )
