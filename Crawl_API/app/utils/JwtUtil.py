import jwt
from app.core.config import SECRET_KEY
from app.core.response import api_response

ALGORITHM = "HS256"

def extract_role(token: str) -> str:

    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])

        authorities = payload.get("authorities")

        if authorities and isinstance(authorities, list) and len(authorities) > 0:
            return str(authorities[0])

        return None

    except jwt.ExpiredSignatureError:
        return api_response(
            success=False,
            message="Expired token",
            data={},
            status=401
        )
    except Exception:
        return api_response(
            success=False,
            message="Invalid token",
            data={},
            status=401
        )