package com.qlik.assignment.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class Utility {
  /**
   * Get the username from the token
   *
   * @param token inputToken
   * @return userName from the token
   */
  public static String getUserName(String token) {
    String trimToken = token.substring("Bearer ".length());
    Algorithm algorithm =
        Algorithm.HMAC256(
            "secret"
                .getBytes(
                    StandardCharsets
                        .UTF_8)); // Need to load this secrets value from Vault or GCP secret
    JWTVerifier verifier = JWT.require(algorithm).build();
    DecodedJWT decodedJWT = verifier.verify(trimToken);
    return decodedJWT.getSubject();
  }

  /**
   * Checks if the given string is palindrome
   *
   * @param s inputString
   * @return Boolean value
   */
  public static Boolean checkIfPalindrome(String s) {
    String str = s.replaceAll("\\s", "");
    String reverseStr = "";
    int strLength = str.length();
    for (int i = (strLength - 1); i >= 0; --i) {
      reverseStr = reverseStr + str.charAt(i);
    }
    return str.equalsIgnoreCase(reverseStr.toLowerCase());
  }
}
