package com.qlik.assignment.exception;

import javax.naming.AuthenticationException;

public class JWTMalformedException extends AuthenticationException {

  private static final long serialVersionUID = 1L;

  public JWTMalformedException(String msg) {
    super(msg);
  }
}
