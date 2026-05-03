# Tasks: rabbitmq-config

## 1. RabbitMQ Configuration

- [x] 1.1 Create RabbitMQConfig.java in config package with @Configuration annotation
- [x] 1.2 Add queue name constants as public static final strings
- [x] 1.3 Declare main payments.queue as durable with dead letter exchange argument
- [x] 1.4 Declare dead letter queue as durable
- [x] 1.5 Declare DirectExchange for main payments exchange
- [x] 1.6 Declare DirectExchange for dead letter exchange
- [x] 1.7 Declare Binding between main queue and main exchange using routing key
- [x] 1.8 Declare Binding between dead letter queue and dead letter exchange
- [x] 1.9 Add JacksonJsonMessageConverter bean for JSON messaging
- [x] 1.10 Add RabbitTemplate bean using the JSON message converter

## 2. App Configuration

- [x] 2.1 Create AppConfig.java in config package with @Configuration annotation
- [x] 2.2 Add RestTemplate bean for calling Mock Payment Service

## 3. Testing

- [ ] 3.1 Run the application and confirm it starts without errors
- [ ] 3.2 Open RabbitMQ management UI at localhost:15672
- [ ] 3.3 Confirm payments.queue exists under Queues tab
- [ ] 3.4 Confirm payments.dead.letter.queue exists under Queues tab
- [ ] 3.5 Confirm payments.exchange exists under Exchanges tab