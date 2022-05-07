package com.micro.gatewayserver.filter;
import com.nimbusds.jose.JWSObject;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import com.micro.gatewayserver.temp.TempBodyForLogin;

import java.text.ParseException;
import java.util.List;
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
        String token = serverHttpRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        //check

        //
//        JSONObject jsonObject=new JSONObject();
//        jsonObject.put("usrName","root usr");
//        jsonObject.put("usrId","root usr");
//        jsonObject.put("authorities","root usr");
//        String base64 = Base64.getEncoder().encodeToString(jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));
        ServerHttpRequest request = exchange.getRequest().mutate().header("usrName", "rootUsr").build();
        request = exchange.getRequest().mutate().header("usrId", "rootUsr").build();
        request = exchange.getRequest().mutate().header("usrRole", "rootUsr").build();
        exchange  = exchange.mutate().request(request).build();
        return chain.filter(exchange);
    }
}
