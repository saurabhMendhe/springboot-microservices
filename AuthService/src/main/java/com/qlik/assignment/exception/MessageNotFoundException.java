package com.qlik.assignment.exception;

public class MessageNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;

  public MessageNotFoundException(String msg) {
    super(msg);
  }
}
