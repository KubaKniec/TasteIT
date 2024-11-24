from fastapi import Request
from motor.motor_asyncio import AsyncIOMotorDatabase

async def get_database(request: Request) -> AsyncIOMotorDatabase:
    return request.app.mongodb

async def get_all_posts(database: AsyncIOMotorDatabase):
    collection = database["post"]
    posts = await collection.find().to_list(None)
    return posts

async def save_clusters(clusters, database: AsyncIOMotorDatabase):
    collection = database["clusters"]
    await collection.insert_one({"clusters": clusters})
