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
- `shared-domain`: Shared library containing common models, DTOs, and utilities.
- `log-generator`: (Planned) Tool for generating simulated log traffic.

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

## Development Guidelines
- Follow Spring Boot best practices.
- Use Lombok for boilerplate reduction.
- Ensure all microservices are registered/routed via the `api-gateway`.
- Shared entities should reside in `shared-domain`.
