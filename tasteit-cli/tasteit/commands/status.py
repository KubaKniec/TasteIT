import click
import subprocess
from colorama import Fore, Style
from ..utils.docker_manager import DockerManager


@click.command()
@click.option('--detailed', is_flag=True, help='Show detailed container information')
def status(detailed):

    docker_manager = DockerManager()

    click.echo(f"{Fore.CYAN}📊 TasteIT Services Status{Style.RESET_ALL}")
    click.echo("=" * 50)

    all_services = ['tasteit-db', 'kafka', 'kafka-ui', 'tasteit-api', 'tasteit-ml', 'tasteit-frontend', 'zookeeper']

    running_services = []

    for service in all_services:
        port = docker_manager.service_ports.get(service)
        if port:
            is_running = docker_manager._is_port_open('localhost', port)

            service_info = {
                'tasteit-db': 'MongoDB Database',
                'kafka': 'Apache Kafka',
                'kafka-ui': 'Kafka UI Dashboard',
                'tasteit-api': 'Spring Boot API',
                'tasteit-ml': 'ML Service (Python)',
                'tasteit-frontend': 'Angular Frontend',
                'zookeeper': 'Zookeeper'
            }

            description = service_info.get(service, service)

            if is_running:
                status_icon = f"{Fore.GREEN}🟢 RUNNING{Style.RESET_ALL}"
                url = f"http://localhost:{port}"
                click.echo(f"{status_icon} {description:20} {url}")
                running_services.append(service)
            else:
                status_icon = f"{Fore.RED}🔴 STOPPED{Style.RESET_ALL}"
                click.echo(f"{status_icon} {description:20} Port {port}")

    click.echo("=" * 50)

    if running_services:
        click.echo(f"{Fore.GREEN}✅ {len(running_services)} service(s) running{Style.RESET_ALL}")

        if detailed:
            click.echo(f"\n{Fore.CYAN}📋 Container Details:{Style.RESET_ALL}")
            _show_container_details()
    else:
        click.echo(f"{Fore.YELLOW}⚠️  No TasteIT services are currently running{Style.RESET_ALL}")
        click.echo(f"{Fore.CYAN}💡 Use 'tasteit run <profile>' to start services{Style.RESET_ALL}")


def _show_container_details():
    try:
        result = subprocess.run([
            'docker', 'ps', '--format',
            'table {{.Names}}\t{{.Status}}\t{{.Ports}}'
        ], capture_output=True, text=True, check=True)

        lines = result.stdout.split('\n')
        header = lines[0] if lines else ""

        relevant_containers = []
        for line in lines[1:]:
            if line and ('tasteit' in line.lower() or 'kafka' in line.lower() or 'zookeeper' in line.lower()):
                relevant_containers.append(line)

        if relevant_containers:
            click.echo(f"\n{header}")
            click.echo("-" * 80)
            for container in relevant_containers:
                click.echo(container)

    except subprocess.CalledProcessError:
        click.echo(f"{Fore.YELLOW}Could not retrieve container details{Style.RESET_ALL}")