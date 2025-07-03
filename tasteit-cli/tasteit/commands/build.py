import click
import subprocess
import os
from colorama import Fore, Style


@click.command()
@click.option('--service', help='Build specific service only (api, ml, frontend)')
@click.option('--no-cache', is_flag=True, help='Build without using cache')
@click.option('--pull', is_flag=True, help='Always pull newer versions of base images')
def build(service, no_cache, pull):
    """Build TasteIT services

    This command builds Docker images for TasteIT services.
    \b
    Available services:
    • api        - Spring Boot API (TasteITServer)
    • ml         - Python ML Service (ml-services)
    • frontend   - Angular Frontend (MixItApp)
    • all        - Build all services (default)
    """

    if service and service not in ['api', 'ml', 'frontend', 'all']:
        click.echo(f"{Fore.RED}❌ Unknown service: {service}{Style.RESET_ALL}")
        click.echo(f"{Fore.YELLOW}Available services: api, ml, frontend, all{Style.RESET_ALL}")
        return

    services_to_build = _get_services_to_build(service)

    click.echo(f"{Fore.CYAN}🔨 Building TasteIT services...{Style.RESET_ALL}")

    for svc in services_to_build:
        _build_service(svc, no_cache, pull)

    click.echo(f"{Fore.GREEN}✅ Build process completed{Style.RESET_ALL}")


def _get_services_to_build(service: str):
    if service == 'api':
        return ['api']
    elif service == 'ml':
        return ['ml']
    elif service == 'frontend':
        return ['frontend']
    else:
        return ['api', 'ml', 'frontend']


def _build_service(service: str, no_cache: bool, pull: bool):

    service_config = {
        'api': {
            'name': 'TasteIT API (Spring Boot)',
            'context': './TasteITServer',
            'dockerfile': 'Dockerfile',
            'image_name': 'tasteit-api'
        },
        'ml': {
            'name': 'TasteIT ML Service (Python)',
            'context': './ml-services',
            'dockerfile': 'Dockerfile',
            'image_name': 'tasteit-ml'
        },
        'frontend': {
            'name': 'TasteIT Frontend (Angular)',
            'context': './MixItApp',
            'dockerfile': 'Dockerfile',
            'image_name': 'tasteit-frontend'
        }
    }

    if service not in service_config:
        click.echo(f"{Fore.RED}❌ Unknown service configuration: {service}{Style.RESET_ALL}")
        return

    config = service_config[service]

    if not os.path.exists(config['context']):
        click.echo(f"{Fore.RED}❌ Directory not found: {config['context']}{Style.RESET_ALL}")
        return

    dockerfile_path = os.path.join(config['context'], config['dockerfile'])
    if not os.path.exists(dockerfile_path):
        click.echo(f"{Fore.YELLOW}⚠️  Dockerfile not found: {dockerfile_path}{Style.RESET_ALL}")
        click.echo(f"{Fore.YELLOW}Creating basic Dockerfile...{Style.RESET_ALL}")
        _create_dockerfile(service, config['context'])

    click.echo(f"{Fore.YELLOW}🔨 Building {config['name']}...{Style.RESET_ALL}")

    try:
        cmd = ['docker', 'build']

        if no_cache:
            cmd.append('--no-cache')

        if pull:
            cmd.append('--pull')

        cmd.extend([
            '-t', config['image_name'],
            '-f', dockerfile_path,
            config['context']
        ])

        click.echo(f"{Fore.CYAN}Running: {' '.join(cmd)}{Style.RESET_ALL}")

        result = subprocess.run(cmd, check=True)

        if result.returncode == 0:
            click.echo(f"{Fore.GREEN}✅ Successfully built {config['name']}{Style.RESET_ALL}")

    except subprocess.CalledProcessError as e:
        click.echo(f"{Fore.RED}❌ Failed to build {config['name']}: {e}{Style.RESET_ALL}")
    except Exception as e:
        click.echo(f"{Fore.RED}❌ Unexpected error building {config['name']}: {str(e)}{Style.RESET_ALL}")


def _create_dockerfile(service: str, context_path: str):
    dockerfiles = {
        'api': """FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Install Maven
RUN apt-get update && apt-get install -y maven

# Build application
RUN mvn clean package -DskipTests

# Run application
EXPOSE 8080
CMD ["java", "-jar", "target/*.jar"]
""",
        'ml': """FROM python:3.9-slim

WORKDIR /app

# Copy requirements
COPY requirements.txt .

# Install dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy application
COPY . .

# Expose port
EXPOSE 8000

# Run application
CMD ["python", "main.py"]
""",
        'frontend': """FROM node:18-alpine as build

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy source
COPY . .

# Build application
RUN npm run build

# Serve with nginx
FROM nginx:alpine
COPY --from=build /app/dist/* /usr/share/nginx/html/
EXPOSE 4200
CMD ["nginx", "-g", "daemon off;"]
"""
    }

    dockerfile_content = dockerfiles.get(service, '')
    if dockerfile_content:
        dockerfile_path = os.path.join(context_path, 'Dockerfile')

        with open(dockerfile_path, 'w') as f:
            f.write(dockerfile_content)

        click.echo(f"{Fore.GREEN}✅ Created basic Dockerfile: {dockerfile_path}{Style.RESET_ALL}")
        click.echo(f"{Fore.YELLOW}💡 You may need to customize this Dockerfile for your specific needs{Style.RESET_ALL}")
    else:
        click.echo(f"{Fore.RED}❌ Could not create Dockerfile for service: {service}{Style.RESET_ALL}")