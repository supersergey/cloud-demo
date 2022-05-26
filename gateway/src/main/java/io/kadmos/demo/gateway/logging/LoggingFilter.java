package io.kadmos.demo.gateway.logging;

import java.net.URI;
import java.util.Collections;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;

@Service
public class LoggingFilter implements GlobalFilter {
    private final Log logger = LogFactory.getLog(LoggingFilter.class);

    @Override
    public Mono<Void> filter(
            final ServerWebExchange exchange,
            final GatewayFilterChain chain
    ) {
        Set<URI> uris = exchange.getAttributeOrDefault(
                GATEWAY_ORIGINAL_REQUEST_URL_ATTR,
                Collections.emptySet()
        );
        String originalUri = (uris.isEmpty())
                ? "Unknown" : uris.iterator().next().toString();
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        URI routeUri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        if (route != null) {
            logger.info("Incoming request "
                    + originalUri
                    + " is routed to id: "
                    + route.getId()
                    + ", uri:" + routeUri);
        }
        return chain.filter(exchange);
    }
}
