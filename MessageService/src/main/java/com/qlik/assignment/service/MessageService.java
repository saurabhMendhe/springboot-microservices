package com.qlik.assignment.service;

import com.qlik.assignment.domain.Message;
import com.qlik.assignment.exception.DatabaseOperationFailedException;
import com.qlik.assignment.exception.MessageNotFoundException;

import java.util.HashMap;
import java.util.List;

/** */
public interface MessageService {

  /**
   * @param message
   * @param token
   * @return
   * @throws DatabaseOperationFailedException
   */
  Message saveMessage(String message, String token) throws DatabaseOperationFailedException;

  /**
   * @param message
   * @param token
   * @return
   * @throws MessageNotFoundException
   * @throws DatabaseOperationFailedException
   */
  HashMap<String, Object> getMessage(String message, String token)
      throws MessageNotFoundException, DatabaseOperationFailedException;

  /**
   * @return
   * @throws DatabaseOperationFailedException
   */
  List<Message> getMessages() throws DatabaseOperationFailedException;

  /**
   * @param message
   * @param token
   * @throws MessageNotFoundException
   * @throws DatabaseOperationFailedException
   */
  void deleteMessage(String message, String token)
      throws MessageNotFoundException, DatabaseOperationFailedException;
}
