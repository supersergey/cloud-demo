package io.kadmos.demo.e2e.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class E2ETest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    public void shouldReturnACorrectAmountFor2SingleOperations() {
        var initialAmountA = getInitialAmount("a");
        var initialAmountB = getInitialAmount("b");
        when().get("/savings/a/balance")
            .then()
            .statusCode(200)
            .body(equalTo(expectedValue(0, initialAmountA)));
        when().get("/savings/b/balance")
            .then()
            .statusCode(200)
            .body(equalTo(expectedValue(0, initialAmountB)));
        given().request().body(/*language=JSON*/ """
                {"amount":  "1.99"}
                """)
            .contentType(ContentType.JSON)
            .when().post("/savings/a/balance")
            .then()
            .statusCode(200)
            .body(equalTo(expectedValue(1.99, initialAmountA))
            );
        given().body(/*language=JSON*/ """
                {"amount":  "2.99"}
                """)
            .contentType(ContentType.JSON)
            .when()
            .post("/savings/b/balance")
            .then()
            .statusCode(200)
            .body(equalTo(expectedValue(2.99, initialAmountB)));
        when().get("/savings/a/balance")
            .then()
            .statusCode(200)
            .body(equalTo(expectedValue(1.99, initialAmountA)));
        when().get("/savings/b/balance")
            .then()
            .statusCode(200)
            .body(equalTo(expectedValue(2.99, initialAmountB)));
    }

    @Test
    public void shouldReturnACorrectAmountAfterParallelRequests() throws Exception {
        var initialAmountA = getInitialAmount("a");
        var initialAmountB = getInitialAmount("b");
        var executors = Executors.newFixedThreadPool(20);
        var tasks = new ArrayList<Callable<Void>>();
        for (int i = 0; i < 10; i++) {
            tasks.add(new Task(1, "a"));
            tasks.add(new Task(2, "b"));
        }
        executors.invokeAll(tasks);
        when().get("/savings/a/balance")
            .then()
            .statusCode(200)
            .body(equalTo(expectedValue(100, initialAmountA)));
        when().get("/savings/b/balance")
            .then()
            .statusCode(200)
            .body(equalTo(expectedValue(200, initialAmountB)));
    }

    private BigDecimal getInitialAmount(String serviceId) {
        var amount = when().get("/savings/" + serviceId + "/balance")
            .andReturn()
            .body()
            .jsonPath().getDouble("amount");
     return BigDecimal.valueOf(amount).setScale(2, RoundingMode.DOWN);
    }

    private String expectedValue(double value, BigDecimal initialAmount) {
        return  "{\"amount\":" + BigDecimal.valueOf(value).add(initialAmount) + "}";
    }

    static class Task implements Callable<Void> {
        private final String amount;
        private final String serviceId;

        public Task(int amount, String serviceId) {
            this.amount = String.valueOf(amount);
            this.serviceId = serviceId;
        }

        @Override
        public Void call() {
            for (int i = 0; i < 10; i++) {
                given().body("{\"amount\": \"" + amount + "\"}")
                    .contentType(ContentType.JSON)
                    .when()
                    .post("savings/" + serviceId + "/balance");
            }
            return null;
        }
    }
}
