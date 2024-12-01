import asyncio
import json
import os
from datetime import datetime
from kafka import KafkaConsumer, KafkaProducer
from user_preferences_analysis.preference_analysis_service import PreferenceAnalysisService

KAFKA_BROKER_URL = os.getenv("KAFKA_BROKER_URL", "localhost:29092")
REQUEST_TOPIC = "preference-analysis-request"
RESPONSE_TOPIC = "preference-analysis-response"


class PreferenceAnalysisConsumer:
    def __init__(self):
        self._init_kafka()
        self.analysis_service = PreferenceAnalysisService()
        self.running = False
        self._consumer_task = None

    def _init_kafka(self):
        try:
            print(f"Initializing Kafka connection to {KAFKA_BROKER_URL}")
            self.consumer = KafkaConsumer(
                REQUEST_TOPIC,
                bootstrap_servers=KAFKA_BROKER_URL,
                group_id='preference-group',
                auto_offset_reset='earliest',
                enable_auto_commit=True,
                max_poll_interval_ms=600000,
                value_deserializer=lambda x: json.loads(x.decode('utf-8')) if x else None
            )
            print("Successfully connected to Kafka consumer")

            self.producer = KafkaProducer(
                bootstrap_servers=KAFKA_BROKER_URL,
                value_serializer=lambda x: json.dumps(x).encode('utf-8')
            )
            print("Successfully connected to Kafka producer")
        except Exception as e:
            print(f"Error initializing Kafka connections: {str(e)}")
            raise

    async def listen_to_messages(self):
        while self.running:
            try:
                msg_pack = self.consumer.poll(timeout_ms=1000)
                if msg_pack:
                    for tp, messages in msg_pack.items():
                        for message in messages:
                            if not self.running:
                                break
                            await self.process_message(message.value)

                await asyncio.sleep(0.1)
            except Exception as e:
                print(f"Error in message processing loop: {str(e)}")
                # Próba ponownego połączenia
                try:
                    print("Attempting to reconnect to Kafka...")
                    self.consumer.close()
                    self.producer.close()
                    await asyncio.sleep(5)  # Poczekaj 5 sekund przed ponowną próbą
                    self._init_kafka()
                except Exception as reconnect_error:
                    print(f"Failed to reconnect: {str(reconnect_error)}")
                    self.running = False
                    break

    def start(self):
        if not self.running:
            print("Starting Preference Analysis Consumer...")
            try:
                self.running = True
                self._consumer_task = asyncio.create_task(self.listen_to_messages())
                print("Preference Analysis Consumer started successfully")
            except Exception as e:
                print(f"Error starting consumer: {str(e)}")
                self.running = False

    async def stop(self):
        print("Stopping Preference Analysis Consumer...")
        self.running = False
        if self._consumer_task:
            try:
                self._consumer_task.cancel()
                await self._consumer_task
            except Exception as e:
                print(f"Error during consumer task cancellation: {str(e)}")
        try:
            self.consumer.close()
            self.producer.close()
        except Exception as e:
            print(f"Error closing Kafka connections: {str(e)}")
        print("Preference Analysis Consumer stopped")

    async def process_message(self, message):
        correlation_id = None
        try:
            user_data = message.get("userData", {})
            clusters_data = message.get("clustersData", {})
            correlation_id = message.get("correlationId")

            print(f"Received message:")
            print(f"- User ID: {user_data.get('userId')}")
            print(f"- Tags count: {len(user_data.get('tags', []))}")
            print(f"- Actions count: {len(user_data.get('actions', []))}")
            print(f"- Clusters count: {len(clusters_data)}")
            print(f"- Correlation ID: {correlation_id}")

            if not user_data or not clusters_data:
                print(f"Missing required data in message with correlationId {correlation_id}")
                return

            analysis_result = self.analysis_service.analyze_user_preferences(user_data, clusters_data)

            response = {
                "correlationId": correlation_id,
                "userPreferences": analysis_result,
                "status": "success",
                "timestamp": datetime.now().isoformat()
            }

            print(f"Sending response: {json.dumps(response, indent=2)}")

            self.producer.send(
                RESPONSE_TOPIC,
                key=correlation_id.encode('utf-8'),
                value=response
            )
            self.producer.flush()

            print(f"Successfully sent response for correlationId {correlation_id}")
        except Exception as e:
            error_response = {
                "correlationId": correlation_id,
                "status": "error",
                "message": str(e),
                "timestamp": datetime.now().isoformat()
            }

            self.producer.send(
                RESPONSE_TOPIC,
                key=correlation_id.encode('utf-8') if correlation_id else None,
                value=error_response
            )
            self.producer.flush()

            print(f"Error processing message: {str(e)}")