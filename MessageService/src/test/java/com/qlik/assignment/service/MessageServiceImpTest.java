package com.qlik.assignment.service;

import com.qlik.assignment.domain.Message;
import com.qlik.assignment.exception.DatabaseOperationFailedException;
import com.qlik.assignment.repository.MessageRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceImpTest {
  @InjectMocks MessageServiceImp messageService;

  @Mock MessageRepo messageRepo;

  @Test
  void testGetAllMessages() throws DatabaseOperationFailedException {
    Message message1 = new Message(null, "121", "user1");
    Message message2 = new Message(null, "11211", "user1");
    List<Message> messageList = Arrays.asList(message1, message2);
    when(messageRepo.findAll()).thenReturn(messageList);

    List<Message> actualMessageList = messageService.getMessages();
    assertEquals(2, actualMessageList.size());
    verify(messageRepo, times(1)).findAll();
  }
}
