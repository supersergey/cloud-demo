package io.kadmos.demo.gateway;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
class GatewayApplicationTest {
    @Autowired
    private WebTestClient webClient;

    @Test
    public void shouldRouteRequestsToServiceA() {
        stubFor(get("/balance/"))
    }
}