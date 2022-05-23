package io.kadmos.demo.gateway.configuration;

import io.kadmos.demo.gateway.configuration.helpers.IsMatchingAccountIdPredicate;
import io.kadmos.demo.gateway.configuration.helpers.RoutePathFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties(GatewayProperties.class)
public class GatewayConfiguration {
    public static final String SERVICE_A = "service-a";
    public static final String SERVICE_B = "service-b";

    private final GatewayProperties gatewayProperties;

    public GatewayConfiguration(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        var serviceADefinition = getServiceDefinition(SERVICE_A);
        var serviceBDefinition = getServiceDefinition(SERVICE_B);

        return builder.routes()
            .route(SERVICE_A + "_POST", r -> r.method(HttpMethod.POST)
                .and()
                .predicate(new IsMatchingAccountIdPredicate(serviceADefinition))
                .and()
                .uri(RoutePathFactory.getPath(serviceADefinition))
            )
            .route(SERVICE_A + "_GET", r -> r.method(HttpMethod.GET)
                .and()
                .predicate(new IsMatchingAccountIdPredicate(serviceADefinition))
                .and()
                .uri(RoutePathFactory.getPath(serviceADefinition))
            )
            .route(SERVICE_B + "_POST", r -> r.method(HttpMethod.POST)
                .and()
                .predicate(new IsMatchingAccountIdPredicate(serviceBDefinition))
                .and()
                .uri(RoutePathFactory.getPath(serviceBDefinition))
            )
            .route(SERVICE_B + "_GET", r -> r.method(HttpMethod.GET)
                .and()
                .predicate(new IsMatchingAccountIdPredicate(serviceBDefinition))
                .and()
                .uri(RoutePathFactory.getPath(serviceBDefinition))
            ).build();
    }

    private ServiceDefinition getServiceDefinition(String serviceName) {
        return Optional.of(gatewayProperties.serviceDefinitionMap().get(serviceName)).orElseThrow();
    }
}
