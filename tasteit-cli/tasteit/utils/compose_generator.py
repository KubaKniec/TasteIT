import yaml
import os
from typing import Dict, List


class ComposeGenerator:

    def __init__(self):
        self.base_services = self._load_base_services()
        self.profiles = {
            'frontend': ['tasteit-db', 'zookeeper', 'kafka', 'kafka-ui', 'tasteit-api', 'tasteit-ml'],
            'backend': ['tasteit-db', 'zookeeper', 'kafka', 'kafka-ui'],
            'ml': ['tasteit-db', 'zookeeper', 'kafka', 'kafka-ui', 'tasteit-api'],
            'full': ['tasteit-db', 'zookeeper', 'kafka', 'kafka-ui', 'tasteit-api', 'tasteit-ml', 'tasteit-frontend'],
            'minimal': ['tasteit-db', 'zookeeper', 'kafka', 'kafka-ui']
        }

    def _load_base_services(self) -> Dict:
        kafka_vars = {
            'KAFKA_BROKER_ID': 1,
            'KAFKA_ZOOKEEPER_CONNECT': 'zookeeper:2181',
            'KAFKA_ADVERTISED_LISTENERS': 'PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:29092',
            'KAFKA_LISTENER_SECURITY_PROTOCOL_MAP': 'PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT',
            'KAFKA_INTER_BROKER_LISTENER_NAME': 'PLAINTEXT',
            'KAFKA_LISTENERS': 'PLAINTEXT://0.0.0.0:9092,PLAINTEXT_HOST://0.0.0.0:29092',
            'KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR': 1,
            'KAFKA_TRANSACTION_STATE_LOG_MIN_ISR': 1,
            'KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR': 1,
            'KAFKA_MESSAGE_MAX_BYTES': 5242880,
            'KAFKA_REPLICA_FETCH_MAX_BYTES': 5242880
        }

        return {
            'tasteit-db': {
                'image': 'mongo:latest',
                'container_name': 'tasteit-db',
                'ports': ['27017:27017'],
                'environment': {
                    'MONGO_INITDB_ROOT_USERNAME': '${MONGO_USERNAME:-admin}',
                    'MONGO_INITDB_ROOT_PASSWORD': '${MONGO_PASSWORD:-password}',
                    'MONGO_INITDB_DATABASE': '${MONGO_DB_NAME:-tasteit}'
                },
                'volumes': ['mongo-data:/data/db'],
                'networks': ['app-network'],
                'healthcheck': {
                    'test': "echo 'db.runCommand(\"ping\").ok' | mongosh --quiet",
                    'interval': '10s',
                    'timeout': '10s',
                    'retries': 5,
                    'start_period': '40s'
                }
            },
            'zookeeper': {
                'image': 'confluentinc/cp-zookeeper:latest',
                'container_name': 'zookeeper',
                'environment': {
                    'ZOOKEEPER_CLIENT_PORT': 2181,
                    'ZOOKEEPER_TICK_TIME': 2000
                },
                'ports': ['2181:2181'],
                'networks': ['app-network'],
            },
            'kafka': {
                'image': 'confluentinc/cp-kafka:latest',
                'container_name': 'kafka',
                'depends_on': ['zookeeper'],
                'ports': ['9092:9092', '29092:29092'],
                'environment': kafka_vars,
                'networks': ['app-network'],
                'healthcheck': {
                    'test': "bash -c 'cub kafka-ready -b kafka:9092 1 1'",
                    'interval': '10s',
                    'timeout': '5s',
                    'retries': 5,
                    'start_period': '60s'
                }
            },
            'kafka-ui': {
                'image': 'provectuslabs/kafka-ui:latest',
                'container_name': 'kafka-ui',
                'depends_on': ['kafka'],
                'ports': ['8090:8080'],
                'environment': {
                    'KAFKA_CLUSTERS_0_NAME': 'local',
                    'KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS': 'kafka:9092',
                    'KAFKA_CLUSTERS_0_ZOOKEEPER': 'zookeeper:2181'
                },
                'networks': ['app-network']
            },
            'tasteit-api': {
                'build': {
                    'context': './TasteITServer',
                    'dockerfile': 'Dockerfile'
                },
                'container_name': 'tasteit-api',
                'ports': ['8080:8080'],
                'depends_on': {
                    'tasteit-db': {
                        'condition': 'service_healthy'
                    },
                    'kafka': {
                        'condition': 'service_healthy'
                    }
                },
                'environment': {
                    'SPRING_DATA_MONGODB_URI': 'mongodb://${MONGO_USERNAME:-admin}:${MONGO_PASSWORD:-password}@tasteit-db:27017/${MONGO_DB_NAME:-tasteit}?authSource=admin',
                    'SPRING_KAFKA_BOOTSTRAP_SERVERS': 'kafka:9092'
                },
                'networks': ['app-network']
            },
            'tasteit-ml': {
                'build': {
                    'context': './ml-services',
                    'dockerfile': 'Dockerfile'
                },
                'container_name': 'tasteit-ml',
                'ports': ['8000:8000'],
                'depends_on': {
                    'tasteit-db': {
                        'condition': 'service_healthy'
                    },
                    'kafka': {
                        'condition': 'service_healthy'
                    }
                },
                'environment': {
                    'MONGODB_URI': 'mongodb://${MONGO_USERNAME:-admin}:${MONGO_PASSWORD:-password}@tasteit-db:27017/${MONGO_DB_NAME:-tasteit}?authSource=admin',
                    'KAFKA_BOOTSTRAP_SERVERS': 'kafka:9092'
                },
                'networks': ['app-network']
            },
            'tasteit-frontend': {
                'build': {
                    'context': './MixItApp',
                    'dockerfile': 'Dockerfile'
                },
                'container_name': 'tasteit-frontend',
                'ports': ['4200:4200'],
                'depends_on': ['tasteit-api'],
                'environment': {
                    'API_URL': 'http://tasteit-api:8080'
                },
                'networks': ['app-network']
            }
        }

    def generate_for_profile(self, profile: str) -> Dict:
        if profile not in self.profiles:
            raise ValueError(f"Unknown profile: {profile}")

        services_to_include = self.profiles[profile]

        compose_content = {
            'version': '3.8',
            'services': {},
            'networks': {
                'app-network': {
                    'driver': 'bridge'
                }
            },
            'volumes': {
                'mongo-data': None
            }
        }

        for service_name in services_to_include:
            if service_name in self.base_services:
                compose_content['services'][service_name] = self.base_services[service_name]

        return compose_content

    def write_compose_file(self, compose_content: Dict, filename: str) -> str:

        filepath = os.path.join(os.getcwd(), filename)

        with open(filepath, 'w') as f:
            yaml.dump(compose_content, f, default_flow_style=False, sort_keys=False)

        return filepath

    def get_services_for_profile(self, profile: str) -> List[str]:
        return self.profiles.get(profile, [])