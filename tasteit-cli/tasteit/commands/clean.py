import click
import subprocess
import os
from colorama import Fore, Style


@click.command()
@click.option('--all', is_flag=True, help='Clean everything including volumes and images')
@click.option('--volumes', is_flag=True, help='Clean volumes only')
@click.option('--images', is_flag=True, help='Clean TasteIT images only')
@click.option('--compose-files', is_flag=True, help='Clean generated compose files only')
@click.option('--force', is_flag=True, help='Skip confirmation prompts')
def clean(all, volumes, images, compose_files, force):
    """Clean up TasteIT development environment

    This command helps clean up various aspects of your development environment:
    \b
    • Generated docker-compose files
    • Docker containers
    • Docker volumes (with --volumes or --all)
    • Docker images (with --images or --all)
    """

    if not any([all, volumes, images, compose_files]):
        _clean_containers(force)
        _clean_compose_files()
        return

    if all:
        if not force and not click.confirm(
                f"{Fore.YELLOW}⚠️  This will remove ALL TasteIT containers, volumes, images and compose files. Continue?{Style.RESET_ALL}"
        ):
            click.echo(f"{Fore.YELLOW}Operation cancelled{Style.RESET_ALL}")
            return

        _clean_containers(force=True)
        _clean_volumes(force=True)
        _clean_images(force=True)
        _clean_compose_files()
        click.echo(f"{Fore.GREEN}✅ Complete cleanup finished{Style.RESET_ALL}")
        return

    if volumes:
        _clean_volumes(force)

    if images:
        _clean_images(force)

    if compose_files:
        _clean_compose_files()


def _clean_containers(force: bool = False):
    click.echo(f"{Fore.YELLOW}🧹 Cleaning containers...{Style.RESET_ALL}")

    try:
        result = subprocess.run([
            'docker', 'ps', '-aq',
            '--filter', 'name=tasteit'
        ], capture_output=True, text=True, check=False)

        containers = result.stdout.strip().split('\n') if result.stdout.strip() else []

        kafka_result = subprocess.run([
            'docker', 'ps', '-aq',
            '--filter', 'name=kafka'
        ], capture_output=True, text=True, check=False)

        if kafka_result.stdout.strip():
            containers.extend(kafka_result.stdout.strip().split('\n'))

        zoo_result = subprocess.run([
            'docker', 'ps', '-aq',
            '--filter', 'name=zookeeper'
        ], capture_output=True, text=True, check=False)

        if zoo_result.stdout.strip():
            containers.extend(zoo_result.stdout.strip().split('\n'))

        if containers:
            subprocess.run(['docker', 'stop'] + containers,
                           capture_output=True, check=False)

            subprocess.run(['docker', 'rm', '-f'] + containers,
                           capture_output=True, check=False)

            click.echo(f"{Fore.GREEN}✅ Removed {len(containers)} container(s){Style.RESET_ALL}")
        else:
            click.echo(f"{Fore.GREEN}✅ No containers to clean{Style.RESET_ALL}")

    except Exception as e:
        click.echo(f"{Fore.RED}❌ Error cleaning containers: {str(e)}{Style.RESET_ALL}")


def _clean_volumes(force: bool = False):
    if not force and not click.confirm(
            f"{Fore.YELLOW}⚠️  This will remove database data. Are you sure?{Style.RESET_ALL}"
    ):
        click.echo(f"{Fore.YELLOW}Skipping volume cleanup{Style.RESET_ALL}")
        return

    click.echo(f"{Fore.YELLOW}🧹 Cleaning volumes...{Style.RESET_ALL}")

    try:
        volumes_to_remove = ['mongo-data']

        for volume in volumes_to_remove:
            result = subprocess.run(['docker', 'volume', 'rm', volume],
                                    capture_output=True, text=True, check=False)
            if result.returncode == 0:
                click.echo(f"{Fore.GREEN}✅ Removed volume: {volume}{Style.RESET_ALL}")

        subprocess.run(['docker', 'volume', 'prune', '-f'],
                       capture_output=True, check=False)

        click.echo(f"{Fore.GREEN}✅ Volume cleanup completed{Style.RESET_ALL}")

    except Exception as e:
        click.echo(f"{Fore.RED}❌ Error cleaning volumes: {str(e)}{Style.RESET_ALL}")


def _clean_images(force: bool = False):
    if not force and not click.confirm(
            f"{Fore.YELLOW}⚠️  This will remove TasteIT Docker images. Continue?{Style.RESET_ALL}"
    ):
        click.echo(f"{Fore.YELLOW}Skipping image cleanup{Style.RESET_ALL}")
        return

    click.echo(f"{Fore.YELLOW}🧹 Cleaning images...{Style.RESET_ALL}")

    try:
        result = subprocess.run([
            'docker', 'images', '--format', '{{.Repository}}:{{.Tag}}',
            '--filter', 'reference=tasteit*'
        ], capture_output=True, text=True, check=False)

        images = result.stdout.strip().split('\n') if result.stdout.strip() else []

        if images:
            subprocess.run(['docker', 'rmi'] + images,
                           capture_output=True, check=False)
            click.echo(f"{Fore.GREEN}✅ Removed {len(images)} image(s){Style.RESET_ALL}")

        subprocess.run(['docker', 'image', 'prune', '-f'],
                       capture_output=True, check=False)

        click.echo(f"{Fore.GREEN}✅ Image cleanup completed{Style.RESET_ALL}")

    except Exception as e:
        click.echo(f"{Fore.RED}❌ Error cleaning images: {str(e)}{Style.RESET_ALL}")


def _clean_compose_files():
    click.echo(f"{Fore.YELLOW}🧹 Cleaning compose files...{Style.RESET_ALL}")

    try:
        cleaned_files = 0
        for file in os.listdir('.'):
            if file.startswith('docker-compose-') and file.endswith('.yml'):
                try:
                    os.remove(file)
                    cleaned_files += 1
                    click.echo(f"{Fore.GREEN}✅ Removed: {file}{Style.RESET_ALL}")
                except Exception as e:
                    click.echo(f"{Fore.YELLOW}⚠️  Could not remove {file}: {str(e)}{Style.RESET_ALL}")

        if cleaned_files == 0:
            click.echo(f"{Fore.GREEN}✅ No compose files to clean{Style.RESET_ALL}")
        else:
            click.echo(f"{Fore.GREEN}✅ Cleaned {cleaned_files} compose file(s){Style.RESET_ALL}")

    except Exception as e:
        click.echo(f"{Fore.RED}❌ Error cleaning compose files: {str(e)}{Style.RESET_ALL}")