# Kafka Messaging

This repository contains a Spring Boot application. Follow the instructions below to set up and run the application.

## Prerequisites

- Java JDK 17 or later
- Maven 3.6+ or Gradle 7.0+ (depending on your build tool)
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)
- Docker and Docker Compose

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/palmurugan/kafka-avro-messaging.git
cd kafka-avro-messaging
```

### Build the Application

#### Using Gradle
```bash
./gradlew clean build
```

## Setting up Kafka

### Docker Compose Configuration

Create a `docker-compose.yml` file in your project root:

```yaml
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    container_name: zookeeper
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - "2181:2181"

  schema-registry:
    image: confluentinc/cp-schema-registry:latest
    hostname: schema-registry
    depends_on:
      - kafka-broker-1
    ports:
      - "8081:8081"
    environment:
      SCHEMA_REGISTRY_HOST_NAME: schema-registry
      SCHEMA_REGISTRY_KAFKASTORE_CONNECTION_URL: 'zookeeper:2181'
      SCHEMA_REGISTRY_LISTENERS: http://schema-registry:8081
      SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS: PLAINTEXT://kafka-broker-1:9092,PLAINTEXT_INTERNAL://localhost:19092
      SCHEMA_REGISTRY_DEBUG: 'true'

  kafka-broker-1:
    image: confluentinc/cp-kafka:latest
    hostname: kafka-broker-1
    ports:
      - "19092:19092"
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: 'zookeeper:2181'
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_INTERNAL:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka-broker-1:9092,PLAINTEXT_INTERNAL://localhost:19092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1

  kafka-tools:
    image: confluentinc/cp-kafka:latest
    hostname: kafka-tools
    container_name: kafka-tools
    command: ["tail", "-f", "/dev/null"]
    network_mode: "host"

  kafka-manager:
    image: hlebalbau/kafka-manager:latest
    depends_on:
      - kafka-broker-1
      - zookeeper
    environment:
      ZK_HOSTS: zookeeper:2181
      APPLICATION_SECRET: "randomsecret"
    ports:
      - "9000:9000"
```

### Starting Kafka

To start Kafka and its dependencies:

```bash
docker-compose up -d
```

To stop the services:

```bash
docker-compose down
```

### Verifying Kafka Setup

- Kafka broker will be available at: `localhost:19092`
- ZooKeeper will be available at: `localhost:2181`

## Running the Application

### Method 1: Using Maven/Gradle

#### Gradle
```bash
./gradlew bootRun
```

### Method 2: Using JAR file

```bash
java -jar target/<application-name>.jar
```

## Configuration

The application can be configured using `application.properties` or `application.yml` in the `src/main/resources` directory.

Example configuration with Kafka properties:
```yaml
spring:
  application:
    name: your-app-name
  
kafka-config:
  bootstrap-servers: localhost:19092
  schema-registry-url-key: schema.registry.url
  schema-registry-url: http://localhost:8081
  num-of-partitions: 3
  replication-factor: 3

kafka-producer-config:
  key-serializer-class: org.apache.kafka.common.serialization.StringSerializer
  value-serializer-class: io.confluent.kafka.serializers.KafkaAvroSerializer
  compression-type: snappy
  acks: all
  batch-size: 16384
  batch-size-boost-factor: 100
  linger-ms: 5
  request-timeout-ms: 60000
  retry-count: 5

kafka-consumer-config:
  key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
  value-deserializer: io.confluent.kafka.serializers.KafkaAvroDeserializer
  restaurant-approval-consumer-group-id: restaurant-approval-topic-consumer
  auto-offset-reset: earliest
  specific-avro-reader-key: specific.avro.reader
  specific-avro-reader: true
  batch-listener: true
  auto-startup: true
  concurrency-level: 3
  session-timeout-ms: 10000
  heartbeat-interval-ms: 3000
  max-poll-interval-ms: 300000
  max-poll-records: 500
  max-partition-fetch-bytes-default: 1048576
  max-partition-fetch-bytes-boost-factor: 1
  poll-timeout-ms: 150
  product-created-consumer-group-id: product-created-event-topic-consumer
  
server:
  port: 8080

# Add other configuration properties as needed
```

## Health Check

Once the application is running, you can check its status at:
- http://localhost:8080/actuator/health (if Spring Actuator is enabled)

## Additional Documentation

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Boot API Documentation](http://localhost:8080/swagger-ui.html) (if Swagger/OpenAPI is configured)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Confluent Docker Documentation](https://docs.confluent.io/platform/current/installation/docker/overview.html)

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Support

For support and questions, please:
- Open an issue in the repository
- Contact the development team
