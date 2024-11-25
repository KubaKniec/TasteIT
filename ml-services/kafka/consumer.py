import logging

from confluent_kafka import Consumer, Producer, KafkaException, KafkaError
from fastapi import Depends
from content_clustering_service.content_clustering_service import ContentClusteringService
import json
from typing import Dict, Any
from datetime import datetime

from db.queries import get_database


class ClusteringConsumer:
    def __init__(self, kafka_bootstrap_servers: str,):
        self._running = False
        self.kafka_bootstrap_servers = kafka_bootstrap_servers
        self._setup_kafka()

        self.clustering_service = ContentClusteringService(
            n_topics=10,
            min_df=0.01,
            max_df=0.95,
            n_top_words=10
        )
        self.consumer.subscribe(['request-topic'])
    def _setup_kafka(self):
        self.consumer = Consumer({
            'bootstrap.servers': self.kafka_bootstrap_servers,
            'group.id': 'tasteit-group',
            'auto.offset.reset': 'earliest',
            'enable.auto.commit': False,
            'max.poll.interval.ms': 600000  # 10 minutes
        })

        self.producer = Producer({
            'bootstrap.servers': self.kafka_bootstrap_servers,
            'message.max.bytes': 5242880,  # 5MB
            'request.timeout.ms': 30000
        })

    async def save_to_mongodb(self, cluster_summary: Dict, correlation_id: str, database=Depends(get_database)):
        collection = database["topic_clusters"]
        await collection.delete_many({})

        for cluster_id, cluster_data in cluster_summary.items():
            cluster_document = {
                "cluster_id": int(cluster_id),
                "name": cluster_data["name"],
                "main_topics": cluster_data["main_topics"],
                "keyword_weights": cluster_data["keyword_weights"],
                "post_count": cluster_data["post_count"],
                "timestamp": datetime.now(),
                "correlation_id": correlation_id
            }
            await collection.insert_one(cluster_document)

    async def process_message(self, message: Dict[str, Any]):
        try:
            data = json.loads(message['value'])
            correlation_id = data.get('correlationId')
            posts = data.get('posts')
            print(f"Received {len(posts)} posts for clustering")
            self.clustering_service.fit(posts.json())
            clustered_posts = self.clustering_service.predict(posts)
            cluster_summary = self.clustering_service.get_cluster_summary()

            posts_assignments = [
                {
                    "post_id": post.get("postId"),
                    "cluster_id": post.get("clusterId"),
                    "confidence": post.get("clusterConfidence")
                }
                for post in clustered_posts
                if all(key in post for key in ["postId", "clusterId", "clusterConfidence"])
            ]

            await self.save_to_mongodb(cluster_summary, correlation_id)
            response = {
                "status": "success",
                "message": "Clustering completed and saved successfully",
                "total_posts": len(posts),
                "valid_assignments": len(posts_assignments),
                "number_of_clusters": len(cluster_summary),
                "posts_assignments": posts_assignments,
                "correlation_id": correlation_id
            }

            self.producer.produce(
                'response-topic',
                key=correlation_id,
                value=json.dumps(response),
                headers=[('correlation_id', correlation_id.encode('utf-8'))]
            )
            self.producer.flush()


        except Exception as e:
            error_response = {
                "status": "error",
                "message": f"Error during clustering: {str(e)}",
                "correlation_id": message.get('key', 'unknown')
            }

            self.producer.produce(
                'request-topic',
                key=message.get('key', 'unknown'),
                value=json.dumps(error_response),
                headers=[('correlation_id', message.get('key', 'unknown'))]
            )
            self.producer.flush()

    async def start(self):
        try:
            logging.info("Attempting to connect to Kafka...")
            while True:
                msg = self.consumer.poll(1.0)
                if msg is None:
                    continue
                if msg.error():
                    if msg.error().code() == KafkaError._PARTITION_EOF:
                        continue
                    else:
                        logging.error(f"Error: {msg.error()}")
                        raise KafkaException(msg.error())
                await self.process_message({
                    'key': msg.key().decode('utf-8') if msg.key() else None,
                    'value': msg.value().decode('utf-8') if msg.value() else None,
                    'topic': msg.topic(),
                    'partition': msg.partition(),
                    'offset': msg.offset()
                })
        except KafkaException as e:
            logging.error(f"KafkaException occurred: {e}")
        finally:
            self.consumer.close()
            logging.info("Kafka consumer closed.")