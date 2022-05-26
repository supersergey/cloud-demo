package io.kadmos.demo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@ConfigurationProperties(prefix = "service")
@Validated
@ConstructorBinding
public record ServiceProperties(
        @NotNull @Size(min = 1, max = 1) List<UUID> activeAccounts) {
}
