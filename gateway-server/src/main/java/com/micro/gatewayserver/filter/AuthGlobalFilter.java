package com.micro.gatewayserver.filter;
import com.micro.gatewayserver.security.JwtUtils;
import com.nimbusds.jose.JWSObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import com.micro.gatewayserver.temp.TempBodyForLogin;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

@Component
public class AuthGlobalFilter implements GlobalFilter
{
    @Autowired
    private RouterValidator routerValidator;//custom route validator

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${bezkoder.app.jwtSecret}")
    private String jwtSecret;

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
//    {
//        ServerHttpRequest serverHttpRequest = exchange.getRequest();
//        String token = serverHttpRequest.getHeaders().getFirst("token");
////        Flux<DataBuffer> body = serverHttpRequest.getBody();
////        StringBuilder sb = new StringBuilder();
////        body.subscribe(buffer -> {
////            byte[] bytes = new byte[buffer.readableByteCount()];
////            buffer.read(bytes);
////            DataBufferUtils.release(buffer);
////            String bodyString = new String(bytes, StandardCharsets.UTF_8);
////            sb.append(bodyString);
////        });
////        //check
////        System.out.println(sb.toString());
//        //
////        JSONObject jsonObject=new JSONObject();
////        jsonObject.put("usrName","root usr");
////        jsonObject.put("usrId","root usr");
////        jsonObject.put("authorities","root usr");
////        String base64 = Base64.getEncoder().encodeToString(jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));
//        ServerHttpRequest request = exchange.getRequest().mutate().header("usrName", "rootUsr").build();
//        request = exchange.getRequest().mutate().header("usrId", "rootUsr").build();
//        request = exchange.getRequest().mutate().header("usrRole", "rootUsr").build();
//        exchange  = exchange.mutate().request(request).build();
//        return chain.filter(exchange);
//    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request))
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);

            final String token = this.getAuthHeader(request);

            if (!jwtUtils.validateJwtToken(token)) {
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
            }
            else{
                Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
                if(!request.getHeaders().containsKey("usrName") || !claims.get("usrName").equals(request.getHeaders().getFirst("usrName")))
                    return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
            }

            this.populateRequestWithHeaders(exchange, token);
        }
        return chain.filter(exchange);
    }

    /*PRIVATE*/

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        exchange.getRequest().mutate()
                .build();
    }
}

