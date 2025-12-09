import os, argparse, torch
from datasets import load_dataset
from transformers import (
    AutoTokenizer, AutoModelForCausalLM,
    TrainingArguments, BitsAndBytesConfig
)
from peft import LoraConfig, get_peft_model, prepare_model_for_kbit_training
from trl import SFTTrainer

SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
PROJECT_ROOT = os.path.dirname(SCRIPT_DIR)

DEFAULT_MODEL = os.path.join(PROJECT_ROOT, "model", "Qwen2.5-1.5B")
DEFAULT_DATA  = os.path.join(PROJECT_ROOT, "data", "processed", "data_fine_tune.jsonl")
DEFAULT_OUT   = os.path.join(PROJECT_ROOT, "train", "outputs", "qwen2.5-1.5b-comic-intent")


def main():
    ap = argparse.ArgumentParser()

    ap.add_argument("--model_name", default=DEFAULT_MODEL)
    ap.add_argument("--data_path", default=DEFAULT_DATA)
    ap.add_argument("--out_dir", default=DEFAULT_OUT)

    # train intent nhanh là đủ
    ap.add_argument("--epochs", type=int, default=1)
    ap.add_argument("--lr", type=float, default=2e-4)
    ap.add_argument("--max_len", type=int, default=256)

    # hợp VRAM 6GB nhưng giảm step để nhanh hơn
    ap.add_argument("--batch_size", type=int, default=2)
    ap.add_argument("--grad_accum", type=int, default=4)

    ap.add_argument("--seed", type=int, default=42)
    args = ap.parse_args()

    if not os.path.exists(args.model_name):
        raise FileNotFoundError(
            f"Không thấy model ở: {args.model_name}\n"
            f"Bạn kiểm tra lại folder model/Qwen2.5-1.5B"
        )
    if not os.path.exists(args.data_path):
        raise FileNotFoundError(
            f"Không thấy data fine-tune ở: {args.data_path}\n"
            f"Bạn kiểm tra file data/processed/data_fine_tune.jsonl"
        )

    os.makedirs(args.out_dir, exist_ok=True)

    print("PROJECT_ROOT =", PROJECT_ROOT)
    print("MODEL_PATH   =", args.model_name)
    print("DATA_PATH    =", args.data_path)
    print("OUT_DIR      =", args.out_dir)

    bnb_config = BitsAndBytesConfig(
        load_in_4bit=True,
        bnb_4bit_quant_type="nf4",
        bnb_4bit_use_double_quant=True,
        bnb_4bit_compute_dtype=torch.float16,  # <-- FIX: dùng torch.float16
    )

    tokenizer = AutoTokenizer.from_pretrained(
        args.model_name, use_fast=True, trust_remote_code=True
    )
    if tokenizer.pad_token is None:
        tokenizer.pad_token = tokenizer.eos_token

    model = AutoModelForCausalLM.from_pretrained(
        args.model_name,
        quantization_config=bnb_config,
        device_map="auto",
        trust_remote_code=True
    )

    model = prepare_model_for_kbit_training(model)  # <-- quan trọng nhất
    model.enable_input_require_grads()              # <-- đảm bảo input có grad
    model.config.use_cache = False

    lora_config = LoraConfig(
        r=16,
        lora_alpha=32,
        lora_dropout=0.05,
        bias="none",
        task_type="CAUSAL_LM",
        target_modules=[
            "q_proj","k_proj","v_proj","o_proj",
            "up_proj","down_proj","gate_proj"
        ],
    )
    model = get_peft_model(model, lora_config)

    ds = load_dataset("json", data_files=args.data_path, split="train")

    def to_text(example):
        messages = example["messages"]
        text = tokenizer.apply_chat_template(
            messages,
            tokenize=False,
            add_generation_prompt=False
        )
        return {"text": text}

    ds = ds.map(to_text, remove_columns=ds.column_names)

    train_args = TrainingArguments(
        output_dir=args.out_dir,
        num_train_epochs=args.epochs,
        per_device_train_batch_size=args.batch_size,
        gradient_accumulation_steps=args.grad_accum,
        learning_rate=args.lr,
        fp16=True,
        logging_steps=20,
        save_steps=500,
        save_total_limit=2,
        optim="paged_adamw_8bit",
        lr_scheduler_type="cosine",
        warmup_ratio=0.03,
        weight_decay=0.0,
        seed=args.seed,
        report_to="none",
        gradient_checkpointing=True
    )

    trainer = SFTTrainer(
        model=model,
        tokenizer=tokenizer,
        train_dataset=ds,
        dataset_text_field="text",
        max_seq_length=args.max_len,
        args=train_args,
        packing=True
    )

    trainer.train()

    trainer.model.save_pretrained(args.out_dir)
    tokenizer.save_pretrained(args.out_dir)

    print("Done. Saved adapter to:", args.out_dir)


if __name__ == "__main__":
    main()
