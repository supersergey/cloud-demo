package io.kadmos.demo.gateway;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient(timeout = "10s")
class GatewayApplicationTest {
    @Autowired
    private WebTestClient webClient;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void shouldRouteRequestsToServiceA() {
        prepareStubs();
        webClient.get().uri("/savings/a/balance")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.origin").isEqualTo("service-a-GET");
        webClient.post().uri("/savings/a/balance")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.origin").isEqualTo("service-a-POST");
        webClient.get().uri("/savings/b/balance")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.origin").isEqualTo("service-b-GET");
        webClient.post().uri("/savings/b/balance")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.origin").isEqualTo("service-b-POST");
    }

    @Test
    public void onTimeoutShouldReturn504() {
        stubFor(get("/balance-a").willReturn(
            aResponse()
                .withStatus(200)
                .withFixedDelay(2000)
        ));
        webClient.get().uri("/savings/a/balance")
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.GATEWAY_TIMEOUT);
    }

    private void prepareStubs() {
        stubFor(get("/balance-a").willReturn(repliesWithServiceName("service-a-GET")));
        stubFor(post("/balance-a").willReturn(repliesWithServiceName("service-a-POST")));
        stubFor(get("/balance-b").willReturn(repliesWithServiceName("service-b-GET")));
        stubFor(post("/balance-b").willReturn(repliesWithServiceName("service-b-POST")));
    }

    private ResponseDefinitionBuilder repliesWithServiceName(String serviceName) {
        return okJson("{\"origin\":" + serviceName + "}");
    }
}