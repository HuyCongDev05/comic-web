import torch
from transformers import (
    AutoModelForCausalLM,
    AutoTokenizer,
    TrainingArguments,
    Trainer,
    BitsAndBytesConfig,
)
from peft import LoraConfig, get_peft_model
from datasets import load_dataset

model_path = r"D:\comic-web\train AI\model\Qwen2.5-1.5B"
data_path = r"D:\comic-web\train AI\format_data\train_final.jsonl"

print("CUDA available:", torch.cuda.is_available())
print("Num GPUs:", torch.cuda.device_count())
if torch.cuda.is_available():
    print("GPU name:", torch.cuda.get_device_name(0))
else:
    raise SystemError(
        "Không tìm thấy GPU (CUDA). Hãy kiểm tra lại cài đặt driver + PyTorch GPU."
    )

tokenizer = AutoTokenizer.from_pretrained(model_path, trust_remote_code=True)

if tokenizer.pad_token is None:
    tokenizer.pad_token = tokenizer.eos_token

MAX_LEN = 384  # giảm xuống cho vừa 6GB VRAM

dataset = load_dataset("json", data_files=data_path)

def preprocess(examples):
    merged = [
        ins + "\n" + resp
        for ins, resp in zip(examples["instruction"], examples["response"])
    ]

    tokens = tokenizer(
        merged,
        truncation=True,
        padding="max_length",
        max_length=MAX_LEN,
    )

    # labels: mask padding = -100 để không tính loss ở padding
    input_ids = tokens["input_ids"]
    labels = []
    for ids in input_ids:
        labels.append([
            token_id if token_id != tokenizer.pad_token_id else -100
            for token_id in ids
        ])
    tokens["labels"] = labels

    return tokens

dataset = dataset.map(
    preprocess,
    batched=True,
    remove_columns=dataset["train"].column_names,
)

quant_config = BitsAndBytesConfig(
    load_in_4bit=True,
    bnb_4bit_compute_dtype=torch.float16,
    bnb_4bit_use_double_quant=True,
    bnb_4bit_quant_type="nf4",
)

model = AutoModelForCausalLM.from_pretrained(
    model_path,
    quantization_config=quant_config,
    device_map="auto",
    trust_remote_code=True,
)

# Đảm bảo không dùng cache trong training
model.config.use_cache = False

lora_config = LoraConfig(
    r=16,
    lora_alpha=32,
    target_modules=["q_proj", "k_proj", "v_proj", "o_proj"],
    lora_dropout=0.05,
    bias="none",
    task_type="CAUSAL_LM",
)

model = get_peft_model(model, lora_config)
model.print_trainable_parameters()


training_args = TrainingArguments(
    output_dir="./finetuned_out",

    per_device_train_batch_size=2,  # thử 2 nếu đủ VRAM
    gradient_accumulation_steps=8,
    fp16=True,

    learning_rate=2e-4,
    num_train_epochs=1,
    max_steps=3000,     # quan trọng nhất để giảm thời gian train

    logging_steps=50,
    save_steps=2000,
    save_total_limit=2,

    dataloader_num_workers=0,
    report_to="none",
)


trainer = Trainer(
    model=model,
    args=training_args,
    train_dataset=dataset["train"],
)

trainer.train()

save_dir = "./model_finetuned"
trainer.model.save_pretrained(save_dir)
tokenizer.save_pretrained(save_dir)

print("✅ Fine-tune xong, model đã lưu tại:", save_dir)
