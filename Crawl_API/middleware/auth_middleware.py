from fastapi import Request
from starlette.middleware.base import BaseHTTPMiddleware
from app.core.config import SECRET_KEY
from app.core.response import api_response
import jwt

ALGORITHM = "HS256"

class AuthMiddleware(BaseHTTPMiddleware):
    async def dispatch(self, request: Request, call_next):
        public_paths = [""]

        if request.url.path in public_paths:
            return await call_next(request)

        auth_header = request.headers.get("Authorization")

        if not auth_header:
            return api_response(
                success=False,
                message="Invalid token signature",
                data={},
                status=401
            )

        try:
            scheme, token = auth_header.split()

            if scheme.lower() != "bearer":
                return api_response(
                    success=False,
                    message="Invalid token signature",
                    data={},
                    status=401
                )

            # Verify token
            payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])

            # Lưu thông tin user vào request state (dùng được ở controller)
            request.state.user = payload

        except jwt.ExpiredSignatureError:
            return api_response(
                success=False,
                message="Invalid token signature",
                data={},
                status=401
            )
        except jwt.InvalidTokenError:
            return api_response(
                success=False,
                message="Verification Failed",
                data={},
                status=400
            )
        except ValueError:
            return api_response(
                success=False,
                message="Invalid token signature",
                data={},
                status=401
            )
        response = await call_next(request)
        return response