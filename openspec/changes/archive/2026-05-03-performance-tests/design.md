# Design: performance-tests

## Context
The application is now fully built with all reliability features
working. The final step is to prove it performs well under load
and that horizontal scaling actually increases throughput. Gatling
is a load testing tool that sends many concurrent HTTP requests
and generates detailed reports showing response times, throughput
and error rates. We use Gatling's Java API since the project is
already in Java.

## Goals / Non-Goals

**Goals:**
- Add Gatling to the project using Maven plugin
- Write a simulation that sends concurrent POST /payments requests
- Run test with single instance and record results
- Run test with two instances on different ports and record results
- Compare throughput and response times between single and two instances
- Document bottlenecks and suggested optimizations

**Non-Goals:**
- No changes to application code in this change
- No testing of GET /payments endpoint under load
- No testing of database directly
- No automated CI/CD integration for performance tests
- No comparison with three or more instances

## Decisions

**Two instances on different ports over Docker scaling**
For the two instance test, a second instance runs on port 8081
while the first runs on port 8080. Gatling sends requests to
both ports in round robin. This is simpler than Docker Compose
scaling for this assignment. Alternatives considered:
Docker Compose scale — rejected because it requires a load
balancer setup which adds complexity beyond the assignment scope.

## Risks / Trade-offs

[Risk] Mock Payment Service delay of up to 2 seconds limits throughput
- Mitigation: This is expected and documented in test report.
It accurately reflects real world payment processing latency.

[Risk] Single machine running both instances limits scaling results
- Mitigation: Results still show relative improvement. In
production instances would run on separate machines showing
greater gains. This is documented as an assumption in README.

[Risk] Database becomes bottleneck before application scales
- Mitigation: This is a valid finding to document in the test
report as a suggested optimization — connection pooling and
read replicas would be the production solution.