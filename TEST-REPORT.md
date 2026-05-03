# Performance Test Report

## Overview
This report documents the performance testing conducted on the
payment processing application using Gatling. Two tests were run
to compare single instance vs two instance behaviour under load.

## Test Environment
- Machine: MacBook Air
- Java: 26.0.1
- Spring Boot: 4.0.6
- PostgreSQL: 15 (Docker)
- RabbitMQ: 3-management (Docker)
- Mock Payment Service delay: 10ms to 2000ms random
- Test tool: Gatling 3.15.0 with Java API

## Test Methodology
Gatling Java API was used to simulate concurrent POST /payments
requests. Two simulations were run:

1. Single instance on port 8080 — 10 users/second for 60 seconds
   Total: 600 requests

2. Two instances on ports 8080 and 8081 — 10 users/second each
   (20 total) for 60 seconds
   Total: 1200 requests

## Single Instance Results
- Total requests: 600
- Throughput: 10 req/s
- Min response time: 3ms
- Mean response time: 35ms
- 50th percentile: 8ms
- 75th percentile: 13ms
- 95th percentile: 60ms
- 99th percentile: 1,100ms
- Max response time: 1,604ms
- Error rate: 0%

## Two Instance Results
- Total requests: 1,200
- Throughput: 20 req/s
- Min response time: 3ms
- Mean response time: 17ms
- 50th percentile: 8ms
- 75th percentile: 12ms
- 95th percentile: 43ms
- 99th percentile: 271ms
- Max response time: 674ms
- Error rate: 0%

## Comparison

| Metric | Single Instance | Two Instances | Improvement |
|--------|----------------|---------------|-------------|
| Throughput (req/s) | 10 | 20 | 100% increase |
| Mean response time | 35ms | 17ms | 51% faster |
| 95th percentile | 60ms | 43ms | 28% faster |
| 99th percentile | 1,100ms | 271ms | 75% faster |
| Max response time | 1,604ms | 674ms | 58% faster |
| Error rate | 0% | 0% | No degradation |

## Key Observations

Adding a second instance doubled the throughput from 10 to 20 req/s
with zero errors, confirming horizontal scaling works correctly.
Response times also improved as mean dropped from 35ms to 17ms
because two workers process the queue concurrently. The 99th
percentile improved the most, dropping from 1,100ms to 271ms,
which shows that queue backlog was the main cause of occasional
slow responses on single instance.

## Bottlenecks

The main bottleneck I noticed is the Mock Payment Service delay
of up to 2 seconds. This slows down the worker but does not
affect the API response time since the API returns immediately
after saving to the database. 

Under higher load the shared database could also become a
bottleneck since both instances write to the same PostgreSQL
instance at the same time.

## Suggested Optimizations 

Based on what I observed during testing, a few things could
improve performance:

- Adding more RabbitMQ consumer threads per instance so each
  instance can process multiple payments at the same time
- Reducing the outbox scheduler polling interval from 1 second
  to something smaller like 100ms for faster processing
- Increasing the database connection pool size to handle more
  concurrent writes under higher load

## Assumptions

- Both instances ran on the same local machine so results would
  be better in a real deployment where each instance has its
  own dedicated server resources
- The Mock Payment Service was also running in the same application
  instance which means it shared CPU and memory with the worker
- Results are from a development machine and would differ in
  a production environment