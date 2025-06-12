import subprocess
import time
import socket
import os
from typing import List, Dict
from colorama import Fore, Style
import click


class DockerManager:
    def __init__(self):
        self.service_ports = {
            'tasteit-db': 27017,
            'kafka': 9092,
            'kafka-ui': 8090,
            'tasteit-api': 8080,
            'tasteit-ml': 8000,
            'tasteit-frontend': 4200,
            'zookeeper': 2181
        }

    def stop_all(self):
        try:
            result = subprocess.run(
                ['docker', 'ps', '-q', '--filter', 'name=tasteit'],
                capture_output=True, text=True, check=False
            )

            if result.stdout.strip():
                subprocess.run(['docker', 'stop'] + result.stdout.strip().split('\n'),
                               check=False, capture_output=True)

            for container in ['kafka', 'zookeeper', 'kafka-ui']:
                subprocess.run(['docker', 'stop', container],
                               check=False, capture_output=True)

            for file in os.listdir('.'):
                if file.startswith('docker-compose-') and file.endswith('.yml'):
                    try:
                        os.remove(file)
                    except:
                        pass

        except Exception as e:
            click.echo(f"{Fore.YELLOW}Warning: Could not stop all containers: {str(e)}{Style.RESET_ALL}")

    def build_services(self, compose_file: str, force_rebuild_api: bool = False):
        try:
            build_args = ['docker-compose', '-f', compose_file, 'build']

            if force_rebuild_api:
                click.echo(f"{Fore.YELLOW}🔨 Force rebuilding TasteIT API (no cache)...{Style.RESET_ALL}")
                subprocess.run(['docker', 'build', '--no-cache', '-t', 'tasteit-api', './TasteITServer'],
                               check=True)
                build_args.append('--no-cache')

            subprocess.run(build_args, check=True)

        except subprocess.CalledProcessError as e:
            raise Exception(f"Failed to build services: {e}")

    def start_services(self, compose_file: str):
        try:
            subprocess.run(['docker-compose', '-f', compose_file, 'up', '-d'], check=True)
        except subprocess.CalledProcessError as e:
            raise Exception(f"Failed to start services: {e}")

    def wait_for_services(self, profile: str, timeout: int = 120):
        from .compose_generator import ComposeGenerator

        compose_gen = ComposeGenerator()
        services = compose_gen.get_services_for_profile(profile)

        start_time = time.time()

        for service in services:
            if service in self.service_ports:
                port = self.service_ports[service]

                click.echo(f"{Fore.YELLOW}  Waiting for {service} on port {port}...{Style.RESET_ALL}")

                while time.time() - start_time < timeout:
                    if self._is_port_open('localhost', port):
                        click.echo(f"{Fore.GREEN}  ✅ {service} is ready{Style.RESET_ALL}")
                        break
                    time.sleep(2)
                else:
                    click.echo(f"{Fore.YELLOW}  ⚠️  {service} might not be fully ready yet{Style.RESET_ALL}")

    def _is_port_open(self, host: str, port: int) -> bool:
        try:
            with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
                sock.settimeout(1)
                result = sock.connect_ex((host, port))
                return result == 0
        except:
            return False

    def show_services_status(self, profile: str):
        from .compose_generator import ComposeGenerator

        compose_gen = ComposeGenerator()
        services = compose_gen.get_services_for_profile(profile)

        click.echo(f"\n{Fore.CYAN}📊 Services Status:{Style.RESET_ALL}")

        for service in services:
            if service in self.service_ports:
                port = self.service_ports[service]
                status = "🟢 RUNNING" if self._is_port_open('localhost', port) else "🔴 NOT READY"

                service_info = {
                    'tasteit-db': 'MongoDB Database',
                    'kafka': 'Apache Kafka',
                    'kafka-ui': 'Kafka UI',
                    'tasteit-api': 'Spring Boot API',
                    'tasteit-ml': 'ML Service (Python)',
                    'tasteit-frontend': 'Angular Frontend',
                    'zookeeper': 'Zookeeper'
                }

                description = service_info.get(service, service)
                click.echo(f"  {status} {description} (:{port})")

    def follow_logs(self, compose_file: str):
        try:
            subprocess.run(['docker-compose', '-f', compose_file, 'logs', '-f'], check=True)
        except KeyboardInterrupt:
            click.echo(f"\n{Fore.YELLOW}Stopped following logs{Style.RESET_ALL}")
        except subprocess.CalledProcessError as e:
            raise Exception(f"Failed to show logs: {e}")

    def get_container_logs(self, container_name: str, lines: int = 50):
        try:
            result = subprocess.run(
                ['docker', 'logs', '--tail', str(lines), container_name],
                capture_output=True, text=True, check=True
            )
            return result.stdout
        except subprocess.CalledProcessError:
            return f"Could not get logs for {container_name}"