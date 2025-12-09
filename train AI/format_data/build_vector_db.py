import os, json, argparse
from sentence_transformers import SentenceTransformer
import faiss
import numpy as np

SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))

def read_jsonl(path):
    if not path or not os.path.exists(path):
        return []
    out=[]
    with open(path, "r", encoding="utf-8") as f:
        for line in f:
            line=line.strip()
            if line:
                out.append(json.loads(line))
    return out

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--core", default="data/processed/data_core.jsonl")
    ap.add_argument("--new",  default="data/processed/data_new.jsonl")
    ap.add_argument("--out_dir", default="data/embeddings/faiss_comics")
    ap.add_argument("--model", default="sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2")
    args = ap.parse_args()

    core_path = os.path.join(SCRIPT_DIR, "..", args.core)
    new_path  = os.path.join(SCRIPT_DIR, "..", args.new)
    out_dir   = os.path.join(SCRIPT_DIR, "..", args.out_dir)

    os.makedirs(out_dir, exist_ok=True)

    core = read_jsonl(core_path)
    new  = read_jsonl(new_path)

    stories = new + core

    texts=[]
    metas=[]
    for st in stories:
        name = st.get("name","")
        intro = st.get("intro","")
        cats = ", ".join(st.get("categories") or [])
        text = f"{name}. {cats}. {intro}"
        texts.append(text)
        metas.append({
            "id": st.get("id"),
            "name": name,
            "categories": st.get("categories") or [],
            "updated_at": st.get("updated_at"),
            "source": "new" if st in new else "core"
        })

    print("Embedding with:", args.model)
    emb_model = SentenceTransformer(args.model)
    embs = emb_model.encode(texts, batch_size=64, show_progress_bar=True, normalize_embeddings=True)

    embs = np.array(embs, dtype="float32")
    dim = embs.shape[1]

    index = faiss.IndexFlatIP(dim)
    index.add(embs)

    faiss_path = os.path.join(out_dir, "index.faiss")
    meta_path  = os.path.join(out_dir, "meta.json")

    faiss.write_index(index, faiss_path)
    with open(meta_path, "w", encoding="utf-8") as f:
        json.dump(metas, f, ensure_ascii=False)

    print("Done.")
    print("FAISS:", faiss_path)
    print("META :", meta_path)
    print("Total stories:", len(stories))

if __name__ == "__main__":
    main()
