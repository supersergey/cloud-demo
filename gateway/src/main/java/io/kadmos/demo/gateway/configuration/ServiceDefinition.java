package io.kadmos.demo.gateway.configuration;

import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

public record ServiceDefinition(
    @NotNull URI gatewayPath,
    @NotEmpty List<HttpMethod> methods,
    @NotNull URI baseUri,
    @NotNull URI routingPath
) {}
