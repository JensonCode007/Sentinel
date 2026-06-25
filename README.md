# 🛡️ Sentinel: Real-Time Event-Driven Fraud Detection

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-brightgreen.svg)
![Python](https://img.shields.io/badge/Python-3.11-blue.svg)
![FastAPI](https://img.shields.io/badge/FastAPI-0.100+-teal.svg)
![Kafka](https://img.shields.io/badge/Apache_Kafka-3.9-black.svg)
![Redis](https://img.shields.io/badge/Redis-7.0-red.svg)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?logo=github-actions&logoColor=white)
![Prometheus](https://img.shields.io/badge/Prometheus-E6522C?logo=prometheus&logoColor=white)
![Grafana](https://img.shields.io/badge/Grafana-F46800?logo=grafana&logoColor=white)

## 📖 Overview
Sentinel is a high-throughput, stateful stream processing architecture designed to detect financial fraud in real-time. Moving beyond standard request-driven APIs, Sentinel utilizes an event-driven microservices architecture to process credit card transactions using a dual-layer defense system:

1.  **Fast-Path (Velocity Checks):** An ultra-low latency Redis cache enforces strict transaction velocity limits (e.g., max 5 swipes per 60 seconds per account).
2.  **Slow-Path (Behavioral AI):** A Scikit-Learn `IsolationForest` machine learning model deployed via FastAPI continuously scores high-dimensional transaction vectors in the background to catch complex behavioral anomalies.

## 🏗️ System Architecture

1.  **Ingestion Engine:** Python asyncio firehose blasts mock transaction data at the Spring Boot Gateway.
2.  **State & Persistence:** Spring Boot evaluates the Redis velocity cache. Transactions are persisted to PostgreSQL and simultaneously published to Apache Kafka.
3.  **Stream Processing:** A FastAPI background worker (`aiokafka`) consumes the live Kafka stream asynchronously.
4.  **Anomaly Scoring:** The Machine Learning model calculates an anomaly score for each transaction using 28 Principal Component variables.
5.  **Live Broadcast:** Detected anomalies are pushed to a Next.js frontend instantly via Server-Sent Events (SSE).

## 📊 Observability & Monitoring
To ensure enterprise-grade reliability, Sentinel features a complete observability pipeline tracking JVM vitals, memory heaps, and processing spikes in real-time.

* **Spring Boot Actuator & Micrometer:** Extracts live application metrics and translates them into a time-series format.
* **Prometheus:** Actively scrapes and stores the metric data at 5-second intervals.
* **Grafana:** Visualizes the system heartbeat through a dynamic, real-time dashboard.

![Grafana Dashboard](https://github.com/JensonCode007/Sentinel/blob/master/images/Grafana_Dashboard.png?raw=true)

## ⚙️ CI/CD & Automated Testing
The backend infrastructure is governed by a robust continuous integration pipeline via **GitHub Actions**.
* Every commit triggers an isolated, automated Ubuntu server instance.
* The pipeline utilizes **Docker Service Containers** to spin up live PostgreSQL and Redis instances on the fly.
* **Mockito** and JUnit rigorously test the stateful transaction logic against the ephemeral databases to guarantee zero regressions before merging.

## 💻 Tech Stack

| Component | Technology | Purpose |
| :--- | :--- | :--- |
| **Backend Gateway** | Java 21, Spring Boot | High-speed REST ingestion, routing, and persistence. |
| **ML Microservice** | Python, FastAPI, Scikit-Learn | Asynchronous stream processing and AI anomaly detection. |
| **Message Broker** | Apache Kafka (KRaft Mode) | Decoupling services and ordered event streaming. |
| **In-Memory Cache** | Redis | Microsecond-latency transaction velocity counters. |
| **Database** | PostgreSQL | Long-term immutable ledger of all transactions. |
| **Observability** | Prometheus, Grafana, Micrometer | System tracing, JVM health monitoring, and visualization. |
| **CI/CD** | GitHub Actions, JUnit, Mockito | Automated integration testing and deployment validation. |

## 🚀 Quick Start
Ensure Docker Desktop is running, then spin up the infrastructure:
```bash
docker-compose up -d
