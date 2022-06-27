package com.qlik.assignment.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
  private final List ignoreAuthenticationForUri =
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
          "/auth/user/api-docs/swagger-config");

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (ignoreAuthenticationForUri.contains(request.getServletPath())) {
      filterChain.doFilter(request, response);
    } else {
      log.error(request.getServletPath());
      String authorizationHeader = request.getHeader(AUTHORIZATION);
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        try {
          String token = authorizationHeader.substring("Bearer ".length());
          Algorithm algorithm =
              Algorithm.HMAC256(
                  "secret"
                      .getBytes(
                          StandardCharsets
                              .UTF_8)); // Need to load this secrets value from Vault or GCP secret
          JWTVerifier verifier = JWT.require(algorithm).build();
          DecodedJWT decodedJWT = verifier.verify(token);
          String username = decodedJWT.getSubject();
          String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
          Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
          stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
          UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(username, null, authorities);
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          filterChain.doFilter(request, response);
        } catch (Exception e) {
          log.error("Error : {} ", e.getMessage());
          generateErrorResponse(request, response, e.getMessage());
        }
      } else {
        generateErrorResponse(request, response, "Missing authorization header");
      }
    }
  }

  private void generateErrorResponse(
      HttpServletRequest request, HttpServletResponse response, String errorMsg)
      throws IOException {
    response.setStatus(FORBIDDEN.value());
    Map<String, String> error = new HashMap<>();
    error.put("path", request.getServletPath());
    error.put("error_message", errorMsg);
    response.setContentType(APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getOutputStream(), error);
  }
}
