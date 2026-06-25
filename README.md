# 🛡️ Sentinel: Real-Time Event-Driven Fraud Detection

![Java](https://img.shields.io/badge/Java-21-orange.svg) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-brightgreen.svg) ![Python](https://img.shields.io/badge/Python-3.11-blue.svg) ![FastAPI](https://img.shields.io/badge/FastAPI-0.100+-teal.svg) ![Kafka](https://img.shields.io/badge/Apache_Kafka-3.9-black.svg) ![Redis](https://img.shields.io/badge/Redis-7.0-red.svg) ![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?logo=github-actions&logoColor=white) ![Prometheus](https://img.shields.io/badge/Prometheus-E6522C?logo=prometheus&logoColor=white) ![Grafana](https://img.shields.io/badge/Grafana-F46800?logo=grafana&logoColor=white)

## 📖 Overview
Sentinel is a high-throughput, event-driven microservices architecture for real-time financial fraud detection. It uses a dual-layer defense system:
1. **Fast-Path (Velocity):** Redis cache enforces strict microsecond-latency transaction limits.
2. **Slow-Path (Behavioral AI):** Scikit-Learn `IsolationForest` (via FastAPI) continuously scores complex behavioral anomalies in the background.

## 🏗️ The Pipeline
1. **Ingestion:** Python asyncio firehose streams mock data to the Spring Boot Gateway.
2. **State & Stream:** Spring Boot evaluates Redis velocity, persists to PostgreSQL, and publishes to Apache Kafka.
3. **AI Scoring:** A FastAPI worker consumes the Kafka stream, scoring 28-dimensional vectors for anomalies.
4. **Broadcast:** Anomalies are instantly pushed to a Next.js frontend via Server-Sent Events (SSE).

## ⚡ System Performance & AI Metrics
Sentinel is designed to process financial data before a physical card is removed from a terminal. 
* **Latency:** End-to-end AI processing (Kafka extraction, JSON decoding, and Isolation Forest scoring) averages **8ms to 12ms** per transaction.
* **AI Baseline (Unsupervised):** The system utilizes an unsupervised `IsolationForest` to establish a baseline for zero-day anomaly detection without relying on historical labeled training data. 
    * **True Positives Caught:** 138 anomalous vectors identified successfully.
    * **Recall:** 28.0% | **Precision:** 24.2%

**Live Processing Audit Log:**
```text
2026-06-25 16:46:02,416 - TxID: ce5961ad... | AI Latency: 9.489 ms
✅ [AI PASSED] Normal transaction verified   | Amount: $6.62

2026-06-25 16:46:02,450 - TxID: 72acf53e... | AI Latency: 10.674 ms
🚨 [AI BEHAVIORAL ALERT] Fraud Detected!     | Amount: $5026.26
2026-06-25 16:46:02,460 - TxID: 246444e0... | AI Latency: 9.087 ms

## 📊 Observability & CI/CD
* **Monitoring:** Spring Boot Actuator/Micrometer exposes JVM metrics to **Prometheus**, visualized in real-time via **Grafana**.
* **Automation:** **GitHub Actions** spins up ephemeral Docker containers (Postgres/Redis) to run automated JUnit & Mockito tests on every commit, preventing regressions.

![Grafana Dashboard](https://github.com/JensonCode007/Sentinel/blob/master/images/Grafana_Dashboard.png?raw=true)

## 💻 Tech Stack

| Component | Technology | Purpose |
| :--- | :--- | :--- |
| **Gateway** | Java 21, Spring Boot | REST ingestion, routing, and persistence. |
| **AI Worker** | Python, FastAPI, Scikit-Learn | Async stream processing & anomaly detection. |
| **Broker** | Apache Kafka (KRaft) | Event streaming and service decoupling. |
| **Cache** | Redis | Transaction velocity counters. |
| **Database** | PostgreSQL | Immutable transaction ledger. |
| **DevOps** | Prometheus, Grafana, GitHub Actions | System monitoring and CI/CD validation. |

## 🚀 Quick Start (Local Development)

### 1. Start the Core Infrastructure
Ensure Docker Desktop is running, then spin up the message brokers and cache:
```bash
docker-compose up -d
