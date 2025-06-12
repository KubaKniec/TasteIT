from setuptools import setup, find_packages

setup(
    name="tasteit-cli",
    version="0.1.1",
    description="CLI tool for TasteIT development environment management",
    author="Jakub Konkol",
    packages=find_packages(),
    install_requires=[
        "click>=8.0.0",
        "pyyaml>=6.0",
        "docker>=6.0.0",
        "python-dotenv>=1.0.0",
        "colorama>=0.4.6",
        "psutil>=5.9.0"
    ],
    entry_points={
        "console_scripts": [
            "tasteit=tasteit.cli:cli",
        ],
    },
    python_requires=">=3.8",
    classifiers=[
        "Development Status :: 3 - Alpha",
        "Intended Audience :: Developers",
        "Programming Language :: Python :: 3",
        "Programming Language :: Python :: 3.8",
        "Programming Language :: Python :: 3.9",
        "Programming Language :: Python :: 3.10",
        "Programming Language :: Python :: 3.11",
    ],
)