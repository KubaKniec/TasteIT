import os
from contextlib import asynccontextmanager
from fastapi import FastAPI
import uvicorn
from motor.motor_asyncio import AsyncIOMotorClient
from api.router import router

MONGO_USERNAME = os.getenv("MONGO_USERNAME")
MONGO_PASSWORD = os.getenv("MONGO_PASSWORD")
MONGO_DB_NAME = os.getenv("MONGO_DB_NAME")
MONGO_URL = "mongodb://"+MONGO_USERNAME+':'+MONGO_PASSWORD+"@localhost:27017/"+MONGO_DB_NAME+"?authSource=admin"

@asynccontextmanager
async def startup_mongo(app: FastAPI):
    app.mongodb_client = AsyncIOMotorClient(MONGO_URL)
    app.mongodb = app.mongodb_client[MONGO_DB_NAME]
    print("Connected to MongoDB")
    try:
        yield
    finally:
        app.mongodb_client.close()
        print("Disconnected from MongoDB")

app = FastAPI(lifespan=startup_mongo)
app.include_router(router)

if __name__ == "__main__":
    uvicorn.run(
        "main:app",
        host="0.0.0.0",
        port=3000,
        reload=True
    )