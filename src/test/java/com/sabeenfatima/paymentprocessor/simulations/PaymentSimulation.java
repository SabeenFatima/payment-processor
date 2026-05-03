package com.sabeenfatima.paymentprocessor.simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;


public class PaymentSimulation extends Simulation {
    // Task 2.2: single instance base URL
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    // Task 2.3: POST /payments request
    ScenarioBuilder scenario = scenario("Single Instance Payment Load Test")
            .exec(
                    http("POST /payments")
                            .post("/payments")
                            .body(StringBody("""
                        {
                            "amount": 10.00,
                            "currency": "USD",
                            "description": "Gatling load test"
                        }
                        """))
                            // Task 2.5: assert 202 response
                            .check(status().is(202))
            );

    {
        // Task 2.4: 10 users per second for 60 seconds
        setUp(
                scenario.injectOpen(
                        constantUsersPerSec(10).during(60)
                )
        ).protocols(httpProtocol);
    }
}
