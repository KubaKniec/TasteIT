import click
import subprocess
from colorama import Fore, Style
from ..utils.docker_manager import DockerManager


@click.command()
@click.option('--service', help='Stop specific service only')
@click.option('--force', is_flag=True, help='Force stop and remove containers')
def stop(service, force):
    docker_manager = DockerManager()

    if service:
        click.echo(f"{Fore.YELLOW}⏹️  Stopping {service}...{Style.RESET_ALL}")
        _stop_specific_service(service, force)
    else:
        click.echo(f"{Fore.YELLOW}⏹️  Stopping all TasteIT services...{Style.RESET_ALL}")
        docker_manager.stop_all()

        if force:
            _force_cleanup()

        click.echo(f"{Fore.GREEN}✅ All services stopped{Style.RESET_ALL}")


def _stop_specific_service(service_name: str, force: bool):
    """Stop a specific service"""
    try:
        # Stop the container
        result = subprocess.run(['docker', 'stop', service_name],
                                capture_output=True, text=True, check=False)

        if result.returncode == 0:
            click.echo(f"{Fore.GREEN}✅ {service_name} stopped{Style.RESET_ALL}")

            if force:
                subprocess.run(['docker', 'rm', service_name],
                               capture_output=True, check=False)
                click.echo(f"{Fore.GREEN}✅ {service_name} container removed{Style.RESET_ALL}")
        else:
            click.echo(f"{Fore.YELLOW}⚠️  {service_name} was not running or doesn't exist{Style.RESET_ALL}")

    except Exception as e:
        click.echo(f"{Fore.RED}❌ Error stopping {service_name}: {str(e)}{Style.RESET_ALL}")


def _force_cleanup():
    try:
        click.echo(f"{Fore.YELLOW}🧹 Performing force cleanup...{Style.RESET_ALL}")

        subprocess.run([
            'docker', 'container', 'prune', '-f',
            '--filter', 'label=com.docker.compose.project=tasteit'
        ], capture_output=True, check=False)

        for container in ['kafka', 'zookeeper', 'kafka-ui']:
            subprocess.run(['docker', 'rm', '-f', container],
                           capture_output=True, check=False)

        subprocess.run(['docker', 'network', 'prune', '-f'],
                       capture_output=True, check=False)

        click.echo(f"{Fore.GREEN}✅ Force cleanup completed{Style.RESET_ALL}")

    except Exception as e:
        click.echo(f"{Fore.YELLOW}⚠️  Some cleanup operations failed: {str(e)}{Style.RESET_ALL}")