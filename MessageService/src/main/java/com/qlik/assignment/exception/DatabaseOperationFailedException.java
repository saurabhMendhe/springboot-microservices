package com.qlik.assignment.exception;

public class DatabaseOperationFailedException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * @param msg
   */
  public DatabaseOperationFailedException(String msg) {
    super(msg);
  }
}
