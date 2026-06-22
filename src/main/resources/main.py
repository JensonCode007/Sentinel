from aiokafka import AIOKafkaConsumer
from fastapi import FastAPI
import asyncio
import json
from contextlib import asynccontextmanager
import pandas as pd
from sklearn.ensemble import IsolationForest
import logging
from fastapi.responses import StreamingResponse

KAFKA_BROKER = "localhost:9092"
TOPICS = ["transaction-valid", "transaction-suspicious"]
GROUP_ID = "fraud-analytics-group"
CSV_PATH = "creditcard.csv"

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

iso_forest = IsolationForest(n_estimators=100, contamination=0.01, random_state=42)
frontend_queue = asyncio.Queue()

async def train_ml_model():
    df = pd.read_csv(CSV_PATH)

    feature_cols = [f"V{i}" for i in range(1,29)] + ["Amount"]
    x_train = df[feature_cols].fillna(0)

    iso_forest.fit(x_train.values)



async def consumeKafkaTopics():
    consumer = AIOKafkaConsumer(
        *TOPICS,
        group_id = GROUP_ID,
        bootstrap_servers = KAFKA_BROKER,
        value_deserializer = lambda m: json.loads(m.decode("utf-8")),
        auto_offset_reset="earliest"
    )

    await consumer.start()
    logger.info("Kafka Consumer is listening")
    try:

        async for msg in consumer:
            topic = msg.topic
            transaction = msg.value

            tx_id = transaction.get("id")
            amount = transaction.get("amount")
            mock_account = transaction.get("v1")

            feature_keys = [f"v{i}" for i in range(1,29)] + ["amount"]

            tx_features = [[transaction.get(k,0.0) for k in feature_keys]]

            prediction = iso_forest.predict(tx_features)
            

            if prediction == -1:

                alert = {
                    "tx_id": tx_id,
                    "amount": amount,
                    "type": "BEHAVIORAL_ANOMALY",
                    "status": "BLOCKED"
                }

                await frontend_queue.put(alert)
                print(f"🚨 [AI BEHAVIORAL ALERT] Fraud Detected! | TxID: {tx_id} | Amount: ${amount:.2f}")
            else:
                
                if hash(tx_id) % 100 == 0:
                    print(f"✅ [AI PASSED] Normal transaction verified | Amount: ${amount:.2f}")



    except Exception as e:

        print(f"Kafka Consumer error : {e}")
    finally:
        await consumer.stop()

@asynccontextmanager
async def lifespan(app : FastAPI):
    await train_ml_model()
    consumer_task = asyncio.create_task(consumeKafkaTopics())

    yield

    consumer_task.cancel()



app = FastAPI(lifespan=lifespan)

@app.get("/")
async def health_check():
    return {"status": "Sentinel Brain is online and listening to Kafka."}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8001, reload=True)

@app.get("/stream")
async def stream_alerts():
    async def event_generator():
        while True:
            alert = await frontend_queue.get()
            yield f"data: {json.dumps(alert)}\n\n"

    return StreamingResponse(event_generator(), media_type = "text/event-stream")