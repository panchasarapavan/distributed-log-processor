# Project Memory: Distributed Log Processor

This project is a microservices-based distributed log processing system.

## Tech Stack
- **Java**: 21
- **Framework**: Spring Boot 3.4.1, Spring Cloud 2024.0.0
- **Messaging**: Apache Kafka (KRaft mode)
- **Cache/Rate Limiting**: Redis
- **Database**: PostgreSQL
- **Build Tool**: Maven

## Project Structure
- `api-gateway`: Spring Cloud Gateway for routing and entry point.
- `log-producer`: Service responsible for receiving log events and publishing them to Kafka.
- `log-consumer`: Service that consumes log events from Kafka for processing and persistence.
- `log-generator`: Python and PowerShell scripts for generating simulated log traffic.
- `shared-domain`: Shared library containing common models, DTOs, and utilities.

## Infrastructure (`docker-compose.yml`)
- **Kafka**: Accessible at `localhost:9092`. Topic used: `log-events`.
- **Redis**: Accessible at `localhost:6379`.
- **PostgreSQL**: Database `logprocessor`, user `loguser`, port `5432`.
- **Zipkin**: Distributed tracing at `localhost:9411`.
- **Prometheus**: Metrics collection at `localhost:9090`.
- **Grafana**: Dashboard visualization at `localhost:3000` (User: `admin`, Pass: `admin`).
- **Exporters**:
    - Kafka Exporter: `localhost:9308` (Internal: `kafka-exporter:9308`)
    - Redis Exporter: `localhost:9121` (Internal: `redis-exporter:9121`)
    - Postgres Exporter: `localhost:9187` (Internal: `postgres-exporter:9187`)

## Network Architecture
- All Docker services reside on a dedicated bridge network: `log-network`.
- Exporters connect to their targets using service names (e.g., `redis:6379`).
- Prometheus scrapes exporters using their service names and Java apps using `host.docker.internal`.

## Service Endpoints (Local)
- **API Gateway**: `http://localhost:8080`
- **Log Producer**: `http://localhost:8081` (Routes: `/api/logs/**`)
- **Log Consumer**: `http://localhost:8082` (Routes: `/consumer/health`)

## Tools
- **Log Generator (Python)**: `python .\scripts\log-generator.py`
    - Parameters: `--url`, `--rate` (default 10), `--total` (default 100).
    - Requires: `requests` library (`pip install requests`).
    - **IntelliJ**: A shared run configuration "Log Generator" is available in the `.run` directory.
        - **If you see "Python interpreter is not selected"**:
            1. Open `scripts/log-generator.py` in the editor.
            2. Look for a yellow banner at the top: "Python interpreter is not configured". Click **Configure Python interpreter**.
            3. In the dialog, select **Add Interpreter** -> **System Interpreter**.
            4. Click the **...** button and paste this path: `C:\Users\panch\AppData\Local\Programs\Python\Python312\python.exe`
            5. If you don't see these options, ensure the **Python** plugin is installed (**File > Settings > Plugins**).
- **Log Generator (PowerShell)**: `.\scripts\log-generator.ps1`
    - Parameters: `-Url`, `-RateLimit` (default 10), `-TotalLogs` (default 100).

## Development Guidelines
- Follow Spring Boot best practices.
- Use Lombok for boilerplate reduction.
- Ensure all microservices are registered/routed via the `api-gateway`.
- Shared entities should reside in `shared-domain`.
