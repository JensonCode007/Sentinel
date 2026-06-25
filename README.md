# 🛡️ Sentinel: Real-Time Event-Driven Fraud Detection

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-brightgreen.svg)
![Python](https://img.shields.io/badge/Python-3.11-blue.svg)
![FastAPI](https://img.shields.io/badge/FastAPI-0.100+-teal.svg)
![Kafka](https://img.shields.io/badge/Apache_Kafka-3.9-black.svg)
![Redis](https://img.shields.io/badge/Redis-7.0-red.svg)

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

## 💻 Tech Stack

| Component | Technology | Purpose |
| :--- | :--- | :--- |
| **Backend Gateway** | Java 21, Spring Boot | High-speed REST ingestion, routing, and persistence. |
| **ML Microservice** | Python, FastAPI, Scikit-Learn | Asynchronous stream processing and AI anomaly detection. |
| **Message Broker** | Apache Kafka (KRaft Mode) | Decoupling services and ordered event streaming. |
| **In-Memory Cache** | Redis | Microsecond-latency transaction velocity counters. |
| **Database** | PostgreSQL | Long-term immutable ledger of all transactions. |

## 🚀 Quick Start (Local Development)

### 1. Start the Infrastructure
Ensure Docker Desktop is running, then spin up Kafka and Redis:
```bash
docker-compose up -d
