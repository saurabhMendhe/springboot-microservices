package com.qlik.assignment.controller;

import com.qlik.assignment.domain.Message;
import com.qlik.assignment.service.MessageService;
import com.qlik.assignment.utils.Utility;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MessageController.class)
class MessageControllerTest {

  @MockBean MessageService messageService;

  @Autowired MockMvc mockMvc;

  @Test
  void testGetAllMessage() throws Exception {
    Message message1 = new Message(null, "121", "user1");
    Message message2 = new Message(null, "11211", "user1");
    List<Message> messageList = Arrays.asList(message1, message2);

    Mockito.when(messageService.getMessages()).thenReturn(messageList);

    mockMvc
        .perform(
            get("/api/message")
                .header("Authorization", "Bearer " + Utility.getDummyAccessTokenForUser("user1")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(2)))
        .andExpect(jsonPath("$[0].text", Matchers.is("121")))
        .andExpect(jsonPath("$[0].username", Matchers.is("user1")))
        .andExpect(jsonPath("$[1].text", Matchers.is("11211")))
        .andExpect(jsonPath("$[1].username", Matchers.is("user1")));
  }

  @Test
  void testGetMessageByText() throws Exception {
    String text = "121";
    String access_token = "Bearer " + Utility.getDummyAccessTokenForUser("user1");
    Message message = new Message(null, text, "user1");
    HashMap<String, Object> hm = new HashMap<String, Object>();
    Boolean isPalindrome = com.qlik.assignment.util.Utility.checkIfPalindrome(text);
    hm.put("message", message);
    hm.put("palindrome", isPalindrome.toString());
    Mockito.when(messageService.getMessage(text, access_token)).thenReturn(hm);

    mockMvc
        .perform(get("/api/message/121").header("Authorization", access_token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.palindrome", Matchers.is("true")));
  }

  @Test
  void saveMessage() throws Exception {
    String text = "121";
    String access_token = "Bearer " + Utility.getDummyAccessTokenForUser("user1");
    Message message = new Message(new Long(1), text, "user1");
    Mockito.when(messageService.saveMessage(text, access_token)).thenReturn(message);

    mockMvc
        .perform(post("/api/message").header("Authorization", access_token).content("121"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", Matchers.is(1)))
        .andExpect(jsonPath("$.text", Matchers.is("121")))
        .andExpect(jsonPath("$.username", Matchers.is("user1")));
  }
}
