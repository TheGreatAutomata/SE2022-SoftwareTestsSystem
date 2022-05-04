package com.micro.gatewayserver.filter;
import com.nimbusds.jose.JWSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import com.micro.gatewayserver.temp.TempBodyForLogin;

import java.text.ParseException;
import java.util.Objects;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered
{
    @Override
    public int getOrder()
    {
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        ServerHttpRequest request = serverHttpRequest.mutate().build();
        exchange = exchange.mutate().request(request).build();
        return chain.filter(exchange);
    }
}
