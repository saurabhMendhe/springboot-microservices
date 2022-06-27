package com.qlik.assignment.controller;

import com.qlik.assignment.exception.DatabaseOperationFailedException;
import com.qlik.assignment.exception.MessageNotFoundException;
import com.qlik.assignment.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {
  private final MessageService MessageService;

  @Operation(summary = "Save message information belongs to specific user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(
            responseCode = "500",
            description = "Any error caused by database operation or any exception")
      })
  @PostMapping("/message")
  public ResponseEntity<?> saveMessage(
      @RequestHeader("Authorization") String token, @RequestBody String message) {
    try {
      return ResponseEntity.ok().body(MessageService.saveMessage(message, token));
    } catch (DatabaseOperationFailedException e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "Get all message information belongs to all user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(
            responseCode = "500",
            description = "Any error caused by database operation or any exception")
      })
  @GetMapping("/message")
  public ResponseEntity<?> getAllMessage(@RequestHeader("Authorization") String token) {
    try {
      return ResponseEntity.ok().body(MessageService.getMessages());
    } catch (DatabaseOperationFailedException e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "Delete a message information belongs to specific user by message text")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "Not found - The message was not found"),
        @ApiResponse(
            responseCode = "500",
            description = "Any error caused by database operation or any exception")
      })
  @DeleteMapping("/message/{val}")
  public ResponseEntity<?> deleteMessage(
      @RequestHeader("Authorization") String token, @PathVariable("val") String message) {
    try {
      MessageService.deleteMessage(message, token);
      return ResponseEntity.ok("Record Deleted Successfully");
    } catch (MessageNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (DatabaseOperationFailedException e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "Get a message information belongs to specific user by message text")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "Not found - The message was not found"),
        @ApiResponse(
            responseCode = "500",
            description = "Any error caused by database operation or any exception")
      })
  @GetMapping("/message/{val}")
  public ResponseEntity<?> getMessageById(
      @RequestHeader("Authorization") String token, @PathVariable("val") String message) {
    try {
      final HashMap<String, Object> messageHm = MessageService.getMessage(message, token);
      return ResponseEntity.ok().body(messageHm);
    } catch (MessageNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (DatabaseOperationFailedException e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }
}
