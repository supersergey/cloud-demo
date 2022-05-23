package io.kadmos.demo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@ConfigurationProperties(prefix = "service")
@Validated
@ConstructorBinding
public record ServiceProperties(@NotNull List<UUID> activeAccounts) {
}
