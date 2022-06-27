package com.qlik.assignment.repository;

import com.qlik.assignment.domain.Message;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MessageRepoTest {

  @Autowired MessageRepo messageRepo;

  @Test
  public void testSaveDeleteFind() {
    Message message = new Message(null, "121", "user1");
    messageRepo.save(message);

    Iterable<Message> messages = messageRepo.findAll();
    Assertions.assertThat(messages).extracting(Message::getUsername).containsOnly("user1");

    messageRepo.deleteAll();
    Assertions.assertThat(messageRepo.findAll()).isEmpty();
  }
}
