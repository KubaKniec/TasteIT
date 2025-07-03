import click
import subprocess
from colorama import Fore, Style
from ..utils.docker_manager import DockerManager


@click.command()
@click.argument('service', required=False)
@click.option('--follow', '-f', is_flag=True, help='Follow log output')
@click.option('--lines', '-n', default=50, help='Number of lines to show (default: 50)')
@click.option('--all', is_flag=True, help='Show logs from all services')
def logs(service, follow, lines, all):
    """Show logs from TasteIT services

    Examples:
    \b
    tasteit logs                    # Show available services
    tasteit logs tasteit-api        # Show API logs
    tasteit logs --all              # Show all services logs
    tasteit logs tasteit-api -f     # Follow API logs
    tasteit logs --all --follow     # Follow all logs
    """

    docker_manager = DockerManager()

    available_services = ['tasteit-db', 'tasteit-api', 'tasteit-ml', 'tasteit-frontend', 'kafka', 'zookeeper',
                          'kafka-ui']

    if all:
        _show_all_logs(follow, lines)
    elif service:
        if service not in available_services:
            click.echo(f"{Fore.RED}❌ Unknown service: {service}{Style.RESET_ALL}")
            click.echo(f"{Fore.YELLOW}Available services: {', '.join(available_services)}{Style.RESET_ALL}")
            return

        _show_service_logs(service, follow, lines)
    else:
        click.echo(f"\n{Fore.CYAN}📋 Available services for logs:{Style.RESET_ALL}")

        running_services = []
        for svc in available_services:
            port = docker_manager.service_ports.get(svc)
            if port and docker_manager._is_port_open('localhost', port):
                click.echo(f"{Fore.GREEN}  🟢 {svc}{Style.RESET_ALL}")
                running_services.append(svc)
            else:
                click.echo(f"{Fore.GRAY}  🔴 {svc} (not running){Style.RESET_ALL}")

        if running_services:
            click.echo(f"\n{Fore.CYAN}Usage examples:{Style.RESET_ALL}")
            click.echo(f"  tasteit logs {running_services[0]}")
            click.echo(f"  tasteit logs {running_services[0]} --follow")
            click.echo(f"  tasteit logs --all")
        else:
            click.echo(f"\n{Fore.YELLOW}⚠️  No services are currently running{Style.RESET_ALL}")


def _show_service_logs(service: str, follow: bool, lines: int):
    click.echo(f"{Fore.CYAN}📋 Logs for {service} (last {lines} lines):{Style.RESET_ALL}")
    click.echo("=" * 60)

    try:
        cmd = ['docker', 'logs']

        if follow:
            cmd.append('--follow')
        else:
            cmd.extend(['--tail', str(lines)])

        cmd.append(service)

        if follow:
            click.echo(f"{Fore.YELLOW}Following logs... (Press Ctrl+C to stop){Style.RESET_ALL}")

        subprocess.run(cmd, check=True)

    except subprocess.CalledProcessError:
        click.echo(f"{Fore.RED}❌ Could not get logs for {service}. Is the container running?{Style.RESET_ALL}")
    except KeyboardInterrupt:
        click.echo(f"\n{Fore.YELLOW}Stopped following logs{Style.RESET_ALL}")


def _show_all_logs(follow: bool, lines: int):
    docker_manager = DockerManager()
    try:
        result = subprocess.run([
            'docker', 'ps', '--format', '{{.Names}}',
            '--filter', 'name=tasteit'
        ], capture_output=True, text=True, check=True)

        # Also check for kafka containers
        kafka_result = subprocess.run([
            'docker', 'ps', '--format', '{{.Names}}',
            '--filter', 'name=kafka'
        ], capture_output=True, text=True, check=True)

        zookeeper_result = subprocess.run([
            'docker', 'ps', '--format', '{{.Names}}',
            '--filter', 'name=zookeeper'
        ], capture_output=True, text=True, check=True)

        containers = []
        if result.stdout.strip():
            containers.extend(result.stdout.strip().split('\n'))
        if kafka_result.stdout.strip():
            containers.extend(kafka_result.stdout.strip().split('\n'))
        if zookeeper_result.stdout.strip():
            containers.extend(zookeeper_result.stdout.strip().split('\n'))

        if not containers:
            click.echo(f"{Fore.YELLOW}⚠️  No TasteIT containers are running{Style.RESET_ALL}")
            return

        if follow:
            click.echo(f"{Fore.CYAN}📋 Following logs from all services... (Press Ctrl+C to stop){Style.RESET_ALL}")
            click.echo("=" * 80)

            compose_files = [f for f in os.listdir('.') if f.startswith('docker-compose-') and f.endswith('.yml')]
            if compose_files:
                subprocess.run(['docker-compose', '-f', compose_files[0], 'logs', '-f'], check=True)
            else:
                cmd = ['docker', 'logs', '-f'] + containers
                subprocess.run(cmd, check=True)
        else:
            click.echo(f"{Fore.CYAN}📋 Recent logs from all services:{Style.RESET_ALL}")
            click.echo("=" * 80)

            for container in containers:
                click.echo(f"\n{Fore.YELLOW}--- {container} ---{Style.RESET_ALL}")
                try:
                    subprocess.run(['docker', 'logs', '--tail', str(lines), container], check=True)
                except subprocess.CalledProcessError:
                    click.echo(f"{Fore.RED}Could not get logs for {container}{Style.RESET_ALL}")

    except subprocess.CalledProcessError as e:
        click.echo(f"{Fore.RED}❌ Error getting container list: {e}{Style.RESET_ALL}")
    except KeyboardInterrupt:
        click.echo(f"\n{Fore.YELLOW}Stopped following logs{Style.RESET_ALL}")


import os