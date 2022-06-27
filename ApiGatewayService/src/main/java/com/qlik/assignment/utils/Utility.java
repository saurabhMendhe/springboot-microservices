package com.qlik.assignment.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Preconditions;
import com.qlik.assignment.exception.JWTMalformedException;

import java.nio.charset.StandardCharsets;

public class Utility {
  public static boolean validateToken(String authorizationHeader) throws JWTMalformedException {
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      String token = authorizationHeader.substring("Bearer ".length());
      try {
        Algorithm algorithm =
            Algorithm.HMAC256(
                "secret"
                    .getBytes(
                        StandardCharsets
                            .UTF_8)); // Need to load this secrets value from Vault or GCP secret
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        verifyTokenHeader(decodedJWT);
        return true;
      } catch (Exception ex) {
        throw new JWTMalformedException(ex.getMessage());
      }
    }
    throw new JWTMalformedException("Malformed JWT token");
  }

  private static void verifyTokenHeader(DecodedJWT decodedJWT) throws JWTMalformedException {
    try {
      Preconditions.checkArgument(decodedJWT.getType().equals("JWT"));
    } catch (Exception ex) {
      throw new JWTMalformedException(ex.getMessage());
    }
  }
}
