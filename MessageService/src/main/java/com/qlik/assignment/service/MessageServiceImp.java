package com.qlik.assignment.service;

import com.qlik.assignment.domain.Message;
import com.qlik.assignment.exception.DatabaseOperationFailedException;
import com.qlik.assignment.exception.MessageNotFoundException;
import com.qlik.assignment.repository.MessageRepo;
import com.qlik.assignment.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MessageServiceImp implements MessageService {
  private final MessageRepo messageRepo;

  /**
   * Save the input text and username(from token) into DB
   *
   * @param message input Text
   * @param token access Token in Authorization
   * @return Message Object
   * @throws DatabaseOperationFailedException
   */
  @Override
  public Message saveMessage(String message, String token) throws DatabaseOperationFailedException {
    String username = Utility.getUserName(token);
    Message messageObj = new Message(null, message, username);
    try {
      return messageRepo.save(messageObj);
    } catch (Exception e) {
      throw new DatabaseOperationFailedException(e.getMessage());
    }
  }

  /**
   * Get message information for the user (mentioned in token)
   *
   * @param message input Text
   * @param token access Token in Authorization
   * @return Message Information associated with the user
   * @throws MessageNotFoundException
   * @throws DatabaseOperationFailedException
   */
  @Override
  public HashMap<String, Object> getMessage(String message, String token)
      throws MessageNotFoundException, DatabaseOperationFailedException {
    String username = Utility.getUserName(token);
    try {
      Message messageObj = messageRepo.findByTextAndUsername(message, username);
      if (messageObj != null) {
        HashMap<String, Object> hm = new HashMap<String, Object>();
        Boolean isPalindrome = Utility.checkIfPalindrome(message);
        hm.put("message", messageObj);
        hm.put("palindrome", isPalindrome.toString());
        return hm;
      }
    } catch (Exception e) {
      throw new DatabaseOperationFailedException(e.getMessage());
    }
    throw new MessageNotFoundException("Record not found.");
  }

  /**
   * Gets the list of all messages for every user
   *
   * @return List of all Messages
   * @throws DatabaseOperationFailedException
   */
  @Override
  public List<Message> getMessages() throws DatabaseOperationFailedException {
    try {
      return messageRepo.findAll();
    } catch (Exception e) {
      throw new DatabaseOperationFailedException(e.getMessage());
    }
  }

  /**
   * Delete particular message information for the user (mentioned in token)
   *
   * @param message input Text
   * @param token access Token in Authorization
   * @return Message Information associated with the user
   * @throws MessageNotFoundException
   * @throws DatabaseOperationFailedException
   */
  @Override
  public void deleteMessage(String message, String token)
      throws MessageNotFoundException, DatabaseOperationFailedException {
    String username = Utility.getUserName(token);
    try {
      if (messageRepo.findByTextAndUsername(message, username) != null) {
        messageRepo.deleteByTextAndUsername(message, username);
        return;
      }
    } catch (Exception e) {
      throw new DatabaseOperationFailedException(e.getMessage());
    }
    throw new MessageNotFoundException("Record not found.");
  }
}
