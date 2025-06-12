<p align="center"> 
  <img src="MixITApp/src/assets/icon.png" width="20%" height="20%"> 
</p>


# TasteIT 🍽️ 🚀 - Freshly Cooked Digital Culinary Experience

## 🌟 Project Overview

TasteIT is a cutting-edge culinary social platform, simmering with innovation and ready to serve up the most delicious digital food-sharing experience you've ever tasted! 👨‍🍳🔥

### 🔧 Tech Stack

- **Frontend**: Angular
- **Backend**: Spring Boot
- **TasteIT Recommendation Algorithm**: Java & Python
- **Messaging**: Apache Kafka
- **Containerization**: Docker
- **Database**: MongoDB
- **CLI Tooling**: tasteit-cli - Custom Python CLI for managing the development environment


## 🏗️ Project Structure

```
TasteIT/
│
├── MixITApp/           # Angular Frontend Application
├── TasteITServer/      # Spring Boot Backend Services
├── ml-services/        # Python machine learning services
├── infrastructure/     # Docker and Infrastructure Configurations
└── assets/             # Static Resources
```

## 🔨 🚦 Building and Running

> ⚠️ **Note:** `tasteit-cli` is currently in **experimental version 0.1.0**.  
> It does not yet support all planned commands and features.  
> For now, it's recommended to use only the `minimal` profile for development.

TasteIT uses a custom CLI tool called `tasteit-cli` that simplifies launching and working on the application during development.

## 🚀 Installation

### Global Installation (recommended)

```bash
cd tasteit-cli
pip install -e .
```

After installation, you can use the `tasteit` command from anywhere in your system.

### Local Installation

```bash
cd tasteit-cli
pip install --user -e .
```

## 📋 Requirements

- Python 3.8+
- Docker & Docker Compose
- Access to the root directory of the TasteIT project

## 🛠️ Available Commands

### `tasteit run <profile>`

Starts the development environment with the specified profile.

**Available profiles:**

- `frontend` – Database + API + ML + Kafka (for frontend development)
- `backend` – Database + Kafka (for backend development)
- `ml` – Database + API + Kafka (for ML development)
- `full` – All services in containers
- `minimal` – Only database + Kafka

> ✅ **Recommended for now:** `tasteit run minimal`

**Options:**
- `--build` – Force image rebuild
- `--logs` – Show logs after startup

**Examples:**
```bash
tasteit run frontend            # Start frontend development environment
tasteit run backend --build     # Start with image rebuild
tasteit run full --logs         # Start all services and show logs
```

### `tasteit status`

Displays the status of all TasteIT services.

**Options:**
- `--detailed` – Show detailed container info

```bash
tasteit status                  # Basic status
tasteit status --detailed       # Detailed container information
```

### `tasteit stop`

Stops TasteIT services.

**Options:**
- `--service <name>` – Stop a specific service


## 🧠 Key Features

### 🍳 TasteIT Recommendation Algorithm
- **Spicy Smart Recommendations**: Serving up personalized culinary content


### 📖 Recipe Creation & Browsing
- **Cookbook Crafting**: Intuitive tools for creating and sharing step-by-step recipes

### 🤝 Social Culinary Network
- Follow fellow food enthusiasts
- Comment, react, and engage with culinary content

### 🔒 Privacy & Security
- Rock-solid data protection
- Automatic content filtering
- Safe and tasty environment for all food lovers

### 🚀 High-Performance Architecture
TasteIT is crafted for blazing-fast performance:
- Manually optimized codebase following best software engineering practices
- Efficient, hand-tuned algorithms minimizing computational overhead
- Strategic use of reactive programming and asynchronous processing
- Performance-first design approach, ensuring rapid response times

### 📱 Cross-Platform Support
- Progressive Web App (PWA)
- iOS and Android native app support via Capacitor

---

**Made with ❤️ by the TasteIT Team**