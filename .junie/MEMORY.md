# Project Memory

## Current State (2026-05-18)
The project is initialized with a microservices architecture for log processing.

### Components
1. **API Gateway**: (Port 8080) Configured to route requests to backend services (e.g., `/api/logs/**` to `log-producer`).
2. **Log Producer**: (Port 8081) Ingests logs and sends them to Kafka topic `log-events`. Uses Redis for potential caching/rate-limiting.
3. **Log Consumer**: (Port 8082) Ready to process logs from Kafka.
4. **Shared Domain**: Contains common models used across services.
5. **Infrastructure**: `docker-compose.yml` includes Kafka (9092), Redis (6379), PostgreSQL (5432), Zipkin (9411), Prometheus (9090), and Grafana (3000).

### Recent Actions
- Initialized `.junie/AGENTS.md` with project guidelines and technical overview.
- Verified project structure and core dependencies.
- Added Observability stack (Prometheus, Zipkin, Grafana) to Docker Compose.
- Configured Micrometer Tracing and Prometheus scraping across all microservices.
- Added Prometheus exporters for Kafka, Redis, and PostgreSQL to Docker Compose.
- Updated Prometheus configuration to scrape metrics from the new exporters.
- Fixed Prometheus DNS resolution errors by explicitly defining `log-network` and ensuring all services are on the same network.
- Added restart policies and dependencies to improve infrastructure stability.
- Implemented `log-generator` scripts in Python and PowerShell to simulate log traffic within rate limits.
- Installed Python 3.12 and `requests` library in the environment.
- Configured a shared IntelliJ Run Configuration for the Python log generator.
- Added `scripts/requirements.txt` to facilitate Python interpreter setup in IntelliJ.
- Troubleshooting: Provided detailed instructions and the exact executable path for resolving "Python interpreter is not selected" in IntelliJ.

### Next Steps
- Implement core logic for `log-producer` and `log-consumer`.
- Set up database schemas in PostgreSQL.
