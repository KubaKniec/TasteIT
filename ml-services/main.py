import asyncio
from message_broker.clustering_consumer import ClusteringConsumer
from message_broker.kafka_consumer_service import KafkaConsumerService

async def main():
    loop = asyncio.get_event_loop()
    consumer_service = KafkaConsumerService()

    # Tutaj mozemy dodac dowolna ilosc konsumerow
    # np zrobisz preference_analysis_consumer = PreferenceAnalysisConsumer()
    # consumer_service.add_consumer(preference_analysis_consumer)
    clustering_consumer = ClusteringConsumer()
    consumer_service.add_consumer(clustering_consumer)

    consumer_service.setup_signal_handlers(loop)

    try:
        await consumer_service.start_consumers()
    except KeyboardInterrupt:
        print("Received keyboard interrupt, shutting down...")
        await consumer_service.shutdown()


if __name__ == "__main__":
    asyncio.run(main())