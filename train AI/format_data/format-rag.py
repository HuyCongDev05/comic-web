import pandas as pd
import json

INPUT_FILE = r"D:\comic-web\train AI\data\data_new.xlsx"   # file export từ Excel
OUTPUT_FILE = r"D:\comic-web\train AI\format_data\data_new.jsonl"

SEP = "\t"


df = pd.read_csv(
    INPUT_FILE,
    sep=SEP,
    header=None,
    names=["name", "intro", "chapters", "status", "updated_at", "category"],
    dtype={"name": str, "intro": str, "status": str, "updated_at": str, "category": str},
)

group_cols = ["name", "intro", "chapters", "status", "updated_at"]

def collect_categories(series):
    cats = sorted({g.strip() for g in series.dropna() if isinstance(g, str) and g.strip()})
    return cats

grouped = (
    df
    .groupby(group_cols, dropna=False, as_index=False)["category"]
    .apply(collect_categories)
    .rename(columns={"category": "categories"})
)

with open(OUTPUT_FILE, "w", encoding="utf-8") as f:
    for idx, row in grouped.iterrows():
        name = row["name"] or ""
        intro = row["intro"] or ""
        chapters = row["chapters"]
        status = row["status"] or ""
        updated_at = row["updated_at"] or ""
        categories = row["categories"] or []

        # text dùng cho RAG
        text_parts = [
            name,
            f"Thể loại: {', '.join(categories) if categories else 'Không rõ'}",
            f"Tình trạng: {status}" if status else "",
            f"Số chương: {chapters}" if pd.notna(chapters) else "",
            f"Cập nhật: {updated_at}" if updated_at else "",
            f"Giới thiệu: {intro}",
        ]
        text = "\n".join([p for p in text_parts if p])

        obj = {
            "id": idx + 1,
            "name": name,
            "intro": intro,
            "chapters": float(chapters) if pd.notna(chapters) else None,
            "status": status,
            "updated_at": updated_at,
            "categories": categories,
            "text": text,
        }

        f.write(json.dumps(obj, ensure_ascii=False) + "\n")

print("✅ Đã ghi JSONL RAG vào:", OUTPUT_FILE)
print("Tổng số truyện:", len(grouped))
