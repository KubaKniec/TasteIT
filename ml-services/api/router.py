from fastapi import APIRouter, Depends
from motor.motor_asyncio import AsyncIOMotorDatabase
from db.queries import get_all_posts, get_database

router = APIRouter()

@router.get("/api/getAll")
async def get_all(database: AsyncIOMotorDatabase = Depends(get_database)):
    posts = await get_all_posts(database)
    return posts
@router.post("/api/ml/analyze-topics")
async def analyze_topics_endpoint():
    return {"message": "Clustering completed"}

@router.get("/")
async def root():
    return {"message": "Hello World"}
