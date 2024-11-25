from confluent_kafka import Producer
import json

class KafkaProducerService:
    def __init__(self, kafka_servers):
        self.producer = Producer({
            'bootstrap.servers': kafka_servers,
            'max.request.size': 5242880,
        })

    def send_message(self, topic, key, message):
        def delivery_report(err, msg):
            if err is not None:
                print(f"Message delivery failed: {err}")
            else:
                print(f"Message delivered to {msg.topic()} [{msg.partition()}]")

        self.producer.produce(
            topic=topic,
            key=key,
            value=json.dumps(message).encode('utf-8'),
            callback=delivery_report
        )
        self.producer.flush()
