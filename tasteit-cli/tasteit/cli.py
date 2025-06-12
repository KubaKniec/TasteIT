"""
TasteIT CLI - Development Environment Management Tool
"""
import click
import os
import sys
from colorama import init, Fore, Style

# Initialize colorama for cross-platform colored output
init()

from .commands.run import run
from .commands.status import status
from .commands.stop import stop
from .commands.logs import logs
from .commands.clean import clean
from .commands.build import build


def print_logo():
    logo = f"""
{Fore.CYAN}╔══════════════════════════════════════╗
║              TasteIT CLI             ║
║      Development Environment        ║
║           Management Tool           ║
╚══════════════════════════════════════╝{Style.RESET_ALL}
"""
    click.echo(logo)


@click.group()
@click.version_option(version='0.1.0')
@click.pass_context
def cli(ctx):
    if ctx.invoked_subcommand is None:
        print_logo()
        click.echo(f"{Fore.YELLOW}Use 'tasteit --help' to see available commands{Style.RESET_ALL}")

    if not os.path.exists('docker-compose.yml') and not os.path.exists('./infrastructure'):
        if ctx.invoked_subcommand not in ['--help', '--version']:
            click.echo(f"{Fore.RED}❌ Error: Not in TasteIT project directory{Style.RESET_ALL}")
            click.echo(f"{Fore.YELLOW}Please run this command from the root of your TasteIT project{Style.RESET_ALL}")
            sys.exit(1)


cli.add_command(run)
cli.add_command(status)
cli.add_command(stop)
cli.add_command(logs)
cli.add_command(clean)
cli.add_command(build)

if __name__ == '__main__':
    cli()