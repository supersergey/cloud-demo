package io.kadmos.demo.gateway.configuration.helpers;

import io.kadmos.demo.gateway.configuration.ServiceDefinition;
import org.springframework.web.server.ServerWebExchange;

import java.util.function.Predicate;

public class IsMatchingAccountIdPredicate implements Predicate<ServerWebExchange> {
    private final ServiceDefinition serviceDefinition;

    public IsMatchingAccountIdPredicate(ServiceDefinition serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
    }

    @Override
    public boolean test(ServerWebExchange webExchange) {
        var path = webExchange.getRequest().getPath();
        return path.value().matches(
            "/savings/" +
                RegexPatternFactory.anyOfRegex(serviceDefinition.accounts()) +
                "/balance"
        );
    }
}
