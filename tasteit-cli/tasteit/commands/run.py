"""
Run command implementation
"""
import click
import subprocess
import os
import time
import yaml
from colorama import Fore, Style
from ..utils.docker_manager import DockerManager
from ..utils.compose_generator import ComposeGenerator


@click.command()
@click.argument('profile', type=click.Choice(['frontend', 'backend', 'ml', 'full', 'minimal']))
@click.option('--build', is_flag=True, help='Force rebuild of images before starting')
@click.option('--logs', is_flag=True, help='Show logs after starting services')
def run(profile, build, logs):
    """Run development environment with specified profile

    PROFILES:
    \b
    frontend  - Database + API + ML Service + Kafka (for frontend development)
    backend   - Database + Kafka only (for backend development)
    ml        - Database + API + Kafka (for ML service development)
    full      - All services in containers
    minimal   - Database + Kafka only (minimal setup)
    """

    docker_manager = DockerManager()
    compose_gen = ComposeGenerator()

    click.echo(f"{Fore.CYAN}🚀 Starting TasteIT environment with '{profile}' profile...{Style.RESET_ALL}")

    try:
        click.echo(f"{Fore.YELLOW}⏹️  Stopping existing services...{Style.RESET_ALL}")
        docker_manager.stop_all()
        compose_content = compose_gen.generate_for_profile(profile)
        compose_file_path = compose_gen.write_compose_file(compose_content, f"docker-compose-{profile}.yml")
        if build or profile in ['frontend', 'full', 'ml']:
            click.echo(f"{Fore.YELLOW}🔨 Building services...{Style.RESET_ALL}")
            docker_manager.build_services(compose_file_path, force_rebuild_api=True)

        click.echo(f"{Fore.GREEN}▶️  Starting services...{Style.RESET_ALL}")
        docker_manager.start_services(compose_file_path)

        click.echo(f"{Fore.YELLOW}⏳ Waiting for services to be ready...{Style.RESET_ALL}")
        docker_manager.wait_for_services(profile)
        click.echo(f"{Fore.GREEN}✅ Environment '{profile}' is ready!{Style.RESET_ALL}")
        docker_manager.show_services_status(profile)
        _show_next_steps(profile)
        if logs:
            click.echo(f"{Fore.CYAN}📋 Showing logs (Ctrl+C to stop)...{Style.RESET_ALL}")
            docker_manager.follow_logs(compose_file_path)

    except Exception as e:
        click.echo(f"{Fore.RED}❌ Error starting environment: {str(e)}{Style.RESET_ALL}")
        raise click.ClickException(str(e))


def _show_next_steps(profile):

    instructions = {
        'frontend': [
            "🎯 Frontend development environment ready!",
            "• Backend API is running on http://localhost:8080",
            "• ML Service is running on http://localhost:8000",
            "• MongoDB is running on localhost:27017",
            "• Kafka UI is available at http://localhost:8090",
            "",
            "Next steps:",
            "• cd MixItApp && ng serve (to start Angular frontend)",
            "• Your frontend will connect to the running backend services"
        ],
        'backend': [
            "🎯 Backend development environment ready!",
            "• MongoDB is running on localhost:27017",
            "• Kafka is running on localhost:9092",
            "• Kafka UI is available at http://localhost:8090",
            "",
            "Next steps:",
            "• Start your Spring Boot API in IDE or command line",
            "• API should connect to the running database and Kafka"
        ],
        'ml': [
            "🎯 ML development environment ready!",
            "• Backend API is running on http://localhost:8080",
            "• MongoDB is running on localhost:27017",
            "• Kafka is running on localhost:9092",
            "• Kafka UI is available at http://localhost:8090",
            "",
            "Next steps:",
            "• cd ml-services && python main.py (to start ML service)",
            "• Your ML service will connect to API and Kafka"
        ],
        'full': [
            "🎯 Full environment ready!",
            "• Frontend is running on http://localhost:4200",
            "• Backend API is running on http://localhost:8080",
            "• ML Service is running on http://localhost:8000",
            "• MongoDB is running on localhost:27017",
            "• Kafka UI is available at http://localhost:8090",
            "",
            "🌟 Everything is running in containers - ready to use!"
        ],
        'minimal': [
            "🎯 Minimal environment ready!",
            "• MongoDB is running on localhost:27017",
            "• Kafka is running on localhost:9092",
            "• Kafka UI is available at http://localhost:8090",
            "",
            "Next steps:",
            "• Start your services manually as needed",
            "• They will connect to the running infrastructure"
        ]
    }

    click.echo(f"\n{Fore.CYAN}{'=' * 50}{Style.RESET_ALL}")
    for line in instructions[profile]:
        if line.startswith('🎯'):
            click.echo(f"{Fore.GREEN}{line}{Style.RESET_ALL}")
        elif line.startswith('•'):
            click.echo(f"{Fore.YELLOW}{line}{Style.RESET_ALL}")
        elif line.startswith('Next steps:') or line.startswith('🌟'):
            click.echo(f"{Fore.CYAN}{line}{Style.RESET_ALL}")
        else:
            click.echo(line)
    click.echo(f"{Fore.CYAN}{'=' * 50}{Style.RESET_ALL}")