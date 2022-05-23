package io.kadmos.demo.gateway.configuration;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.UUID;

public record ServiceDefinition(
    @NotNull URI baseUri,
    @NotNull URI path,
    @NotEmpty List<UUID> accounts
) {}
