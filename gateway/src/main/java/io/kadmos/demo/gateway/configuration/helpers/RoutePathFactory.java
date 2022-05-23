package io.kadmos.demo.gateway.configuration.helpers;

import io.kadmos.demo.gateway.configuration.ServiceDefinition;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;

public class RoutePathFactory {
    public static URI getPath(ServiceDefinition serviceDefinition) {
        return new DefaultUriBuilderFactory()
            .builder()
            .host(serviceDefinition.baseUri().toString())
            .path(serviceDefinition.path().toString())
            .build();
    }
}
