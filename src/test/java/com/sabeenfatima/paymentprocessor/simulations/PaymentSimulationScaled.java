package com.sabeenfatima.paymentprocessor.simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class PaymentSimulationScaled extends Simulation {

    // Task 3.2: first instance
    HttpProtocolBuilder httpProtocol1 = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    // Task 3.2: second instance
    HttpProtocolBuilder httpProtocol2 = http
            .baseUrl("http://localhost:8081")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    // Task 3.3: same request for both
    ScenarioBuilder scenario1 = scenario("Instance 1 - Port 8080")
            .exec(
                    http("POST /payments instance 1")
                            .post("/payments")
                            .body(StringBody("""
                        {
                            "amount": 10.00,
                            "currency": "USD",
                            "description": "Gatling scaled test instance 1"
                        }
                        """))
                            .check(status().is(202))
            );

    ScenarioBuilder scenario2 = scenario("Instance 2 - Port 8081")
            .exec(
                    http("POST /payments instance 2")
                            .post("/payments")
                            .body(StringBody("""
                        {
                            "amount": 10.00,
                            "currency": "USD",
                            "description": "Gatling scaled test instance 2"
                        }
                        """))
                            .check(status().is(202))
            );

    {
        // Task 3.4: 10 users per second on each instance = 20 total
        setUp(
                scenario1.injectOpen(
                        constantUsersPerSec(10).during(60)
                ).protocols(httpProtocol1),
                scenario2.injectOpen(
                        constantUsersPerSec(10).during(60)
                ).protocols(httpProtocol2)
        );
    }
}
