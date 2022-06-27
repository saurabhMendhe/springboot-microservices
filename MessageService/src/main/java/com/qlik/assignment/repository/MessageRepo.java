package com.qlik.assignment.repository;

import com.qlik.assignment.domain.Message;
import com.qlik.assignment.exception.DatabaseOperationFailedException;
import com.qlik.assignment.exception.MessageNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface MessageRepo extends JpaRepository<Message, Long> {
  /**
   * @param messageText
   * @param username
   * @return
   * @throws DatabaseOperationFailedException
   */
  Message findByTextAndUsername(String messageText, String username)
      throws DatabaseOperationFailedException;

  /**
   * @param messageText
   * @param username
   * @throws MessageNotFoundException
   * @throws DatabaseOperationFailedException
   */
  @Modifying
  void deleteByTextAndUsername(String messageText, String username)
      throws MessageNotFoundException, DatabaseOperationFailedException;
}
