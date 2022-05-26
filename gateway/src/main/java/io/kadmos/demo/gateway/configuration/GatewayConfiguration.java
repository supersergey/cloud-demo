package io.kadmos.demo.gateway.configuration;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.internal.AtomicRateLimiter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.springframework.cloud.gateway.support.RouteMetadataUtils.CONNECT_TIMEOUT_ATTR;
import static org.springframework.cloud.gateway.support.RouteMetadataUtils.RESPONSE_TIMEOUT_ATTR;

@Configuration
@EnableConfigurationProperties(GatewayProperties.class)
public class GatewayConfiguration {

    private final static String SERVICE_A = "service-a";
    private final static String SERVICE_B = "service-b";

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, GatewayProperties properties) {
        var serviceADefinition = properties.services().get(SERVICE_A);
        var serviceBDefinition = properties.services().get(SERVICE_B);
        return builder.routes()
            .route(p -> p
                .method(serviceADefinition.methods().toArray(HttpMethod[]::new))
                .and()
                .path(serviceADefinition.gatewayPath().getPath())
                .filters(f -> f
                    .rewritePath(serviceADefinition.gatewayPath().getPath(), serviceADefinition.routingPath().getPath()))
                .uri(serviceADefinition.baseUri())
            )
            .route(p -> p
                .method(serviceBDefinition.methods().toArray(HttpMethod[]::new))
                .and()
                .path(serviceBDefinition.gatewayPath().getPath())
                .filters(f -> f
                    .rewritePath(serviceBDefinition.gatewayPath().getPath(), serviceBDefinition.routingPath().getPath())
                )
                .uri(serviceBDefinition.baseUri())
            ).build();
    }

    @Bean
    public RateLimiter rateLimiter() {
        return new AtomicRateLimiter("conf", RateLimiterConfig
            .custom()
            .limitRefreshPeriod(Duration.of(100, ChronoUnit.MILLIS))
            .limitForPeriod(3)
            .build());
    }
}
