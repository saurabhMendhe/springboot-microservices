package com.qlik.assignment.config;

import com.qlik.assignment.filter.GatewayFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
  @Autowired private GatewayFilter filter;

  @Bean
  public RouteLocator routes(RouteLocatorBuilder builder) {
    return builder
        .routes()
        .route(
            "authService",
            r -> r.path("/auth/**").filters(f -> f.filter(filter)).uri("lb://auth-service"))
        .route(
            "messageService",
            r -> r.path("/api/**").filters(f -> f.filter(filter)).uri("lb://message-service"))
        .build();
  }
}
