import re

import regex
from bs4 import BeautifulSoup


def parse_chapter_number(ch_str):
    if not ch_str:
        return None
    match = re.search(r'(\d+(?:\.\d+)?)', str(ch_str))
    if match:
        try:
            return round(float(match.group(1)), 1)
        except Exception:
            return None
    return None


def clean_intro_text(text: str) -> str:
    text = BeautifulSoup(text, "html.parser").get_text()
    text = text.strip()
    text = regex.sub(r"[^\p{L}0-9\s\.]", "", text)
    return text[:1000]
