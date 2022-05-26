package io.kadmos.demo.gateway.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@ConfigurationProperties(prefix = "gateway")
@Validated
public record GatewayProperties(
    @NotEmpty Map<String, ServiceDefinition> services
) { }
