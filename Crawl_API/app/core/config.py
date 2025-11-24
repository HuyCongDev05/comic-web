from dotenv import load_dotenv
import os

load_dotenv()

# Secret key
SECRET_KEY = os.getenv("SECRET_KEY")

# MySQL
DB_CONFIG = {
    "host": os.getenv("DB_HOST"),
    "user": os.getenv("DB_USER"),
    "password": os.getenv("DB_PASSWORD"),
    "database": os.getenv("DB_NAME"),
    "auth_plugin": os.getenv("DB_AUTH_PLUGIN"),
}

# MongoDB
MONGO_URI = os.getenv("MONGO_URI")
MONGO_DB = os.getenv("MONGO_DB")
MONGO_COLLECTION = os.getenv("MONGO_COLLECTION")

# API config
HEADERS = {"User-Agent": os.getenv("HEADERS_USER_AGENT")}
API_BASE = os.getenv("API_BASE")
LIST_API_BASE = os.getenv("LIST_API_BASE")
