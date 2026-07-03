# asynchronous-messaging-kafka

Two Spring Boot microservices communicating asynchronously via Apache Kafka (KRaft mode, no Zookeeper). Built with Java 25 and Gradle.

## Services

| Service | Port | Description |
|---------|------|-------------|
| producer-service | 8082 | REST endpoint to publish `UserOrder` messages to Kafka |
| consumer-service | 8081 | Listens for `UserOrder` messages, validates and stores them |
| Kafka UI | 8080 | Web UI to inspect topics and messages |

## Message Flow

```
POST /kafka/publish (producer :8082)
  → Kafka topic: user-order (3 partitions)
    → KafkaService (consumer :8081)
      ├── totalPrice > 0  → stored in memory → GET /kafka/get-orders
      └── totalPrice <= 0 → DLT (no retry)  → GET /kafka/get-incorrect-orders
```

Transient errors are retried up to 4 times with exponential backoff (1s → 2s → 4s → 8s) across 3 separate retry topics before landing on the DLT.

## UserOrder model

```json
{
  "orderId": 1,
  "dishes": ["pizza", "cola"],
  "totalPrice": 29.99
}
```

---

## Running locally (Docker Compose)

Start Kafka and Kafka UI:

```bash
docker compose up -d
```

Run each service (from its directory):

```bash
cd producer-service && ./gradlew bootRun
cd consumer-service && ./gradlew bootRun
```

Kafka UI: http://localhost:8080

---

## Running on Kubernetes (Minikube)

### 1. Start Minikube

```bash
minikube start
```

### 2. Create GHCR pull secret

```bash
kubectl create secret docker-registry ghcr-secret \
  --docker-server=ghcr.io \
  --docker-username=Surroz \
  --docker-password=<your-github-token>
```

### 3. Apply manifests

```bash
kubectl apply -f k8s/kafka.yml
kubectl apply -f k8s/kafka-ui.yml
kubectl apply -f k8s/producer.yml
kubectl apply -f k8s/consumer.yml
```

### 4. Wait for Kafka to be ready

```bash
kubectl rollout status statefulset/kafka
```

### 5. Access the services

Run port-forwards in the background:

```bash
nohup kubectl port-forward svc/kafka-ui 8080:8080 > /tmp/pf-kafka-ui.log 2>&1 &
nohup kubectl port-forward svc/producer-service 8082:8082 > /tmp/pf-producer.log 2>&1 &
nohup kubectl port-forward svc/consumer-service 8081:8081 > /tmp/pf-consumer.log 2>&1 &
```

| Service | URL |
|---------|-----|
| Kafka UI | http://localhost:8080 |
| Producer | http://localhost:8082 |
| Consumer | http://localhost:8081 |

### Stop everything

```bash
pkill -f "port-forward svc/"
minikube stop
```

---

## API

### Producer

```
POST http://localhost:8082/kafka/publish
Content-Type: application/json

{
  "orderId": 1,
  "dishes": ["pizza", "cola"],
  "totalPrice": 29.99
}
```

### Consumer

```
GET http://localhost:8081/kafka/get-orders            # valid orders
GET http://localhost:8081/kafka/get-incorrect-orders  # DLT orders
```

---

## Build

```bash
# from within each service directory
./gradlew bootJar   # fat JAR
./gradlew test      # run tests
```
