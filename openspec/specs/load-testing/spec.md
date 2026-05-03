# load-testing Specification

## Purpose
TBD - created by archiving change performance-tests. Update Purpose after archive.
## Requirements
### Requirement: Single instance load test
The system SHALL be load tested with a single application instance
to establish a baseline throughput and response time measurement.

#### Scenario: Single instance handles concurrent requests
- **WHEN** Gatling sends 10 concurrent users per second for 60 seconds
- **THEN** all requests SHALL receive 202 Accepted response
- **THEN** throughput SHALL be recorded in requests per second
- **THEN** mean response time SHALL be recorded in milliseconds
- **THEN** 95th percentile response time SHALL be recorded
- **THEN** error rate SHALL be 0% for the API layer

#### Scenario: Single instance results are saved
- **WHEN** Gatling simulation completes
- **THEN** an HTML report SHALL be generated in target/gatling folder
- **THEN** report SHALL show throughput, response times and error rate

### Requirement: Two instance load test
The system SHALL be load tested with two application instances
running concurrently to demonstrate horizontal scaling improves
throughput compared to single instance.

#### Scenario: Two instances handle more concurrent requests
- **WHEN** Gatling sends 20 concurrent users per second for 60 seconds
  across two instances on ports 8080 and 8081
- **THEN** all requests SHALL receive 202 Accepted response
- **THEN** combined throughput SHALL be higher than single instance
- **THEN** mean response time SHALL be similar to or better than single instance
- **THEN** no payment SHALL be lost or duplicated across instances

#### Scenario: Two instance results are saved
- **WHEN** Gatling simulation completes for two instances
- **THEN** an HTML report SHALL be generated in target/gatling folder
- **THEN** report SHALL show higher throughput than single instance run

### Requirement: Performance test report
The system SHALL have a written test report documenting results
from both single and two instance runs with observations and
suggested optimizations.

#### Scenario: Report covers required topics
- **WHEN** test report is reviewed by interviewer
- **THEN** it SHALL contain single instance throughput numbers
- **THEN** it SHALL contain two instance throughput numbers
- **THEN** it SHALL identify at least one bottleneck observed
- **THEN** it SHALL suggest at least two optimizations for production

