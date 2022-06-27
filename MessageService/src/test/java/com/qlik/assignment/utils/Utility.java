package com.qlik.assignment.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Utility {

  public static String getDummyAccessTokenForUser(String username) {

    Algorithm algorithm =
        Algorithm.HMAC256(
            "secret"
                .getBytes(
                    StandardCharsets
                        .UTF_8)); // Need to load this secrets value from Vault or GCP secretManager
    return JWT.create()
        .withSubject(username)
        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10mins
        .withIssuer("test")
        .withIssuer("test.qlik")
        .sign(algorithm);
  }
}
