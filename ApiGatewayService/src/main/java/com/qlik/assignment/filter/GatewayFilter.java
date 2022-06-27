package com.qlik.assignment.filter;

import com.qlik.assignment.utils.Utility;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class GatewayFilter implements org.springframework.cloud.gateway.filter.GatewayFilter {
  private final List<String> ignoreAuthenticationForUri =
      List.of(
          "/auth/user/login",
          "/auth/token/refresh?**",
          "/auth/user/register",
          "/auth/user/api-docs",
          "/auth/user/swagger-ui/index.html",
          "/auth/user/swagger-ui.html",
          "/favicon.ico",
          "/auth/user/swagger-ui/swagger-ui.css",
          "/auth/user/swagger-ui/swagger-ui-bundle.js",
          "/auth/user/swagger-ui/swagger-ui-standalone-preset.js",
          "/auth/user/swagger-ui/favicon-32x32.png",
          "/auth/user/swagger-ui/favicon-16x16.png",
          "/auth/user/api-docs/swagger-config",
          "/api/message/api-docs",
          "/api/message/api-docs/swagger-config",
          "/api/message/swagger-ui.html",
          "/api/message/swagger-ui/index.html",
          "/api/message/swagger-ui/swagger-ui.css",
          "/api/message/swagger-ui/swagger-ui-bundle.js",
          "/api/message/swagger-ui/swagger-ui-standalone-preset.js",
          "/api/message/swagger-ui/favicon-32x32.png",
          "/api/message/swagger-ui/favicon-16x16.png");

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    try {
      ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();
      if (!ignoreAuthenticationForUri.contains(request.getPath().value())) {
        final String token = request.getHeaders().get("Authorization").get(0);
        if (Utility.validateToken(token)) {
          return chain.filter(exchange);
        }
      }
    } catch (Exception e) {
      ServerHttpResponse response = exchange.getResponse();
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      DataBuffer body = response.bufferFactory().wrap(e.getMessage().getBytes());
      response.writeWith(Mono.just(body));
      return response.setComplete();
    }
    return chain.filter(exchange);
  }
}
