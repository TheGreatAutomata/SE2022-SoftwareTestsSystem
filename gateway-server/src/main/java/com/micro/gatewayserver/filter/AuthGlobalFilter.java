package com.micro.gatewayserver.filter;

import com.micro.gatewayserver.permission.CPL;
import com.micro.gatewayserver.permission.RPL;
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
import java.util.*;


import java.text.ParseException;

@Component
public class AuthGlobalFilter implements GlobalFilter {
    @Autowired
    private RouterValidator routerValidator;//custom route validator

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${bezkoder.app.jwtSecret}")
    private String jwtSecret;

    private CPL cpl = new CPL();

    private RPL rpl = new RPL();

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

            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

            //validate UserName
            if (!request.getHeaders().containsKey("usrName") || !claims.get("usrName").equals(request.getHeaders().getFirst("usrName")))
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);

            //validate Role
            var authlist = (ArrayList<LinkedHashMap<String, String>>) claims.get("usrRole");
            String usrRole = request.getHeaders().getFirst("usrRole");
            if(!validateUsrRole(usrRole, authlist))
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);

            //validate privilege
            String path = request.getPath().toString();
            if(usrRole == null || !cpl.getCurPrivilege(usrRole).canAccess(rpl.getRequestPrivilege(path)))
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
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

    private boolean validateUsrRole(String usrRole, ArrayList<LinkedHashMap<String, String>> authList){
        for(var auth: authList){
            if(auth.containsValue(usrRole)){
                return true;
            }
        }
        return false;
    }
}

