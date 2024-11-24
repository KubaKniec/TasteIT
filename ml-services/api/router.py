from datetime import datetime

from fastapi import APIRouter, Depends
import requests
from starlette.responses import JSONResponse

from content_clustering_service.content_clustering_service import ContentClusteringService
from db.queries import get_database


router = APIRouter()
@router.get("/")
async def root():
    return {"message": "Yeah, it's working!"}
@router.get("/api/ml/analyze-topics")
async def analyze_topics_endpoint(database=Depends(get_database)):
    try:
        posts = requests.get('http://192.168.1.108:8080/api/v1/feed/allposts').json()

        if not posts:
            return JSONResponse(
                status_code=400,
                content={"message": "No posts retrieved"}
            )

        clustering_service = ContentClusteringService(
            n_topics=10,
            min_df=0.01,
            max_df=0.95,
            n_top_words=10
        )

        clustering_service.fit(posts)
        clustered_posts = clustering_service.predict(posts)
        cluster_summary = clustering_service.get_cluster_summary()

        posts_assignments = [
            {
                "post_id": post.get("postId"),
                "cluster_id": post.get("clusterId"),
                "confidence": post.get("clusterConfidence")
            }
            for post in clustered_posts
            if all(key in post for key in ["postId", "clusterId", "clusterConfidence"])
        ]

        collection = database["topic_clusters"]
        await collection.delete_many({})
        for cluster_id, cluster_data in cluster_summary.items():
            cluster_document = {
                "cluster_id": int(cluster_id),
                "name": cluster_data["name"],
                "main_topics": cluster_data["main_topics"],
                "keyword_weights": cluster_data["keyword_weights"],
                "post_count": cluster_data["post_count"],
                "timestamp": datetime.now()
            }
            await collection.insert_one(cluster_document)

        return JSONResponse(
            status_code=200,
            content={
                "message": "Clustering completed and saved successfully",
                "total_posts": len(posts),
                "valid_assignments": len(posts_assignments),
                "number_of_clusters": len(cluster_summary),
                "posts_assignments": posts_assignments
            }
        )
    except Exception as e:
        return JSONResponse(
            status_code=500,
            content={"message": f"Error during clustering: {str(e)}"}
        )

