package io.kadmos.demo.gateway.configuration.helpers;

import io.kadmos.demo.gateway.configuration.ServiceDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IsMatchingAccountIdPredicateTest {

    private IsMatchingAccountIdPredicate predicate;

    private final UUID uuid1 = UUID.randomUUID();
    private final UUID uuid2 = UUID.randomUUID();

    @BeforeEach
    void setUp() throws Exception {
        ServiceDefinition serviceDefinition = new ServiceDefinition(
            new URI("http://localhost:8081"),
            new URI("/balance"),
            List.of(uuid1)
        );
        predicate = new IsMatchingAccountIdPredicate(serviceDefinition);
    }

    @Test
    void shouldReturnTrueIfPathMatches(
        @Mock ServerWebExchange exchange,
        @Mock ServerHttpRequest httpRequest,
        @Mock RequestPath requestPath
    ) {
        // given
        when(exchange.getRequest()).thenReturn(httpRequest);
        when(httpRequest.getPath()).thenReturn(requestPath);
        when(requestPath.value()).thenReturn(
            "/savings/" + uuid1 + "/balance"
        );

        // when
        var actual = predicate.test(exchange);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void shouldReturnFalseIfPathDoesNotMatch(
        @Mock ServerWebExchange exchange,
        @Mock ServerHttpRequest httpRequest,
        @Mock RequestPath requestPath
    ) {
        // given
        when(exchange.getRequest()).thenReturn(httpRequest);
        when(httpRequest.getPath()).thenReturn(requestPath);
        when(requestPath.value()).thenReturn(
            "/savings/" + uuid2 + "/balance"
        );

        // when
        var actual = predicate.test(exchange);

        // then
        assertThat(actual).isFalse();
    }
}