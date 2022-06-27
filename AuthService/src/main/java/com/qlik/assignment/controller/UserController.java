package com.qlik.assignment.controller;

import com.qlik.assignment.domain.Role;
import com.qlik.assignment.domain.User;
import com.qlik.assignment.exception.DatabaseOperationFailedException;
import com.qlik.assignment.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class UserController {
  private final UserService userService;

  @Operation(summary = "Get all users information")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(
            responseCode = "500",
            description = "Any error caused by database operation or any exception")
      })
  @GetMapping("/users")
  public ResponseEntity<?> getUsers() {
    try {
      return ResponseEntity.ok().body(userService.getUsers());
    } catch (DatabaseOperationFailedException e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "Register the user")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = User.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Any error caused by database operation or any exception")
      })
  @PostMapping("/user/register")
  public ResponseEntity<?> registerUser(@RequestBody User newUser) {
    try {
      URI uri =
          URI.create(
              ServletUriComponentsBuilder.fromCurrentContextPath()
                  .path("/auth/user/register")
                  .toUriString());
      return ResponseEntity.created(uri).body(userService.saveUser(newUser));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "Save the new role")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Role.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Any error caused by database operation or any exception")
      })
  @PostMapping("/role/save")
  public ResponseEntity<?> saveRole(@RequestBody Role role) {
    try {
      URI uri =
          URI.create(
              ServletUriComponentsBuilder.fromCurrentContextPath()
                  .path("/auth/role/save")
                  .toUriString());
      return ResponseEntity.created(uri).body(userService.saveRole(role));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "Add the role for the user")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = RoleToUserForm.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Any error caused by database operation or any exception")
      })
  @PostMapping("/role/addtouser")
  public ResponseEntity<?> roleAddToUser(@RequestBody RoleToUserForm form) {
    try {
      userService.addRoleToUser(form.getUsername(), form.getRoleName());
      return ResponseEntity.ok().body("Role added successfully");
    } catch (DatabaseOperationFailedException e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "Add the role for the user")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(
            responseCode = "500",
            description = "Any error caused by database operation or any exception")
      })
  @GetMapping("/token/refresh")
  public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) {
    try {
      String access_token = userService.refreshTokenForUser(token);
      Map<String, String> tokens = new HashMap<>();
      tokens.put("access_token", access_token);
      return ResponseEntity.ok().body(tokens);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }
}

@Data
class RoleToUserForm {
  private String username;
  private String roleName;
}
