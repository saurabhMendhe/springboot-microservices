package com.qlik.assignment.exception;

public class DatabaseOperationFailedException extends Exception {

  private static final long serialVersionUID = 1L;

  public DatabaseOperationFailedException(String msg) {
    super(msg);
  }
}
