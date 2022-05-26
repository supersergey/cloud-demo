# Sergiy Tolokunsky Assignment
[My LinkedIn Profile](https://www.linkedin.com/in/sergiy-tolokunsky-103a2b1/)

## System requirements

1. JDK 17
2. Docker Engine 18.06.0+
3. docker-compose plugin supporting 3.7+ `docker-compose.yml` definition

## Running instructions
1. Clone the git repository: `git clone git@github.com:supersergey/cloud-demo.git`
2. Build the project: `./gradlew dockerBuild`
3. Run `docker-compose up`

This will spin-up:
1. Postgres database, port `5432`
2. Two Account services, ports `8081` and `8082`
3. Gateway, port `8080`.

Please make sure these ports are not occupied by other applications.

## Sample requests

Check the balance:
```
curl localhost:8080/savings/a/balance
curl localhost:8080/savings/b/balance
```

Add `10.99` balance:
```
curl -X POST --location "http://localhost:8080/savings/a/balance" \
    -H "Content-Type: application/json" \
    -d "{\"amount\": \"10.99\"}"
```

## Corner cases
There is no verification on negative balance. 

Although basic payload validation is in place, there are no checks on different weird payloads.

There might be many other corner cases about dealing with financial transactions, I guess covering those goes 
beyond the assignment scope.

## End-to-end/Load test

There is the E2E test under the `e2e-tests` folder, which simulates a concurrent load on the system.
It runs against the local environment, therefore it is not included into the build pipeline.

To run the E2E test manually:
1. Spin-up the environment: `docker-compose up`
2. Run `./gradlew e2e-tests:test -Pe2eTest`

**Important**: the E2E test puts test data into your database. It won't be cleaned up automatically.
Make sure you provide a test environment before running the E2E test. 

### Performance considerations
Although the E2E test should be running normally on any modern PC, I experienced network disconnection
when trying to run it on a 2015 computer. This is a good area for investigation. 

## How would you...
> test the timeouts?

There is a timeout test on the gateway layer. By using `WireMock`, I simulate a timeout on the underlying service,
and expect to receive the `504 Gateway timeout` response.

Alternatively, this might be done on the E2E level, by substituting the real underlying service with 
the `Wiremock` stand-alone instance. 

> scale your API gateway?

We may run many app instances in `Kubernetes`, `Consul` or other environments that provide rich functionality for 
scaling and load-balancing. 

> monitor uptime so you can sleep at night?

By using `Spring actuator` (already there) + `Prometheus`, exporting metrics to `DataDog` or `Graphana`, setting up 
alerts in `OpsGenie`.

> Secure your gateway

With JWT tokens, a direction to move in is described [Spring Documentation](https://spring.io/blog/2019/08/16/securing-services-with-spring-cloud-gateway) 