import jwt
import base64
from fastapi import Request
from starlette.middleware.base import BaseHTTPMiddleware
from app.core.config import SECRET_KEY
from app.core.response import api_response

class AuthMiddleware(BaseHTTPMiddleware):
    async def dispatch(self, request: Request, call_next):
        auth_header = request.headers.get("Authorization")
        if not auth_header or not auth_header.startswith("Bearer "):
            return api_response(False, "Missing or invalid Authorization header", {}, 401)

        token = auth_header[7:].strip()
        secret_key_bytes = base64.b64decode(SECRET_KEY)
        try:
            payload = jwt.decode(token, secret_key_bytes, algorithms=['HS256'])
            authorities = payload.get("authorities", [])
            if "ADMIN" not in authorities:
                return api_response(False, "Verification Failed", {}, 403)

            request.state.user = payload

        except jwt.ExpiredSignatureError:
            return api_response(False, "Token expired", {}, 401)
        except jwt.InvalidTokenError:
            return api_response(False, "Invalid token", {}, 401)

        return await call_next(request)