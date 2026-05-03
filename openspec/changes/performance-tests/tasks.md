# Tasks: performance-tests

## 1. Gatling Setup

- [x] 1.1 Add Gatling Maven plugin to pom.xml
- [x] 1.2 Add Gatling Java dependency to pom.xml
- [x] 1.3 Create simulations folder in test directory

## 2. Single Instance Simulation

- [x] 2.1 Create PaymentSimulation.java in simulations folder
- [x] 2.2 Configure base URL pointing to localhost:8080
- [x] 2.3 Define POST /payments request with valid JSON body
- [x] 2.4 Set up scenario with constantUsersPerSec(10) for 60 seconds
- [x] 2.5 Add assertion that all responses are 202 Accepted

## 3. Two Instance Simulation

- [x] 3.1 Create PaymentSimulationScaled.java in simulations folder
- [x] 3.2 Configure two base URLs pointing to localhost:8080 and localhost:8081
- [x] 3.3 Define same POST /payments request for both instances
- [x] 3.4 Set up scenario with constantUsersPerSec(20) split across both instances
- [x] 3.5 Add assertion that all responses are 202 Accepted

## 4. Run Single Instance Test

- [ ] 4.1 Start app on port 8080 (normal IntelliJ run)
- [ ] 4.2 Run mvn gatling:test -Dgatling.simulationClass=...PaymentSimulation
- [ ] 4.3 Open generated HTML report in target/gatling folder
- [ ] 4.4 Record throughput, mean response time and 95th percentile

## 5. Run Two Instance Test

- [ ] 5.1 Keep first instance running on port 8080
- [ ] 5.2 Start second instance on port 8081 by adding server.port=8081 to run config
- [ ] 5.3 Run mvn gatling:test -Dgatling.simulationClass=...PaymentSimulationScaled
- [ ] 5.4 Open generated HTML report in target/gatling folder
- [ ] 5.5 Record throughput, mean response time and 95th percentile

## 6. Test Report

- [ ] 6.1 Create TEST-REPORT.md in project root
- [ ] 6.2 Document single instance results with numbers from Gatling report
- [ ] 6.3 Document two instance results with numbers from Gatling report
- [ ] 6.4 Compare throughput improvement between single and two instances
- [ ] 6.5 Document at least one bottleneck observed
- [ ] 6.6 Suggest at least two optimizations for production