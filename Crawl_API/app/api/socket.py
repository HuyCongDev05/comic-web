from typing import Set

from fastapi import WebSocket

active_connections: Set[WebSocket] = set()


async def send_ws_message(message: str):
    for ws in list(active_connections):
        try:
            await ws.send_text(message)
        except:
            active_connections.discard(ws)
