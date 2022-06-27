package com.qlik.assignment.service;

import com.qlik.assignment.domain.Role;
import com.qlik.assignment.domain.User;
import com.qlik.assignment.exception.DatabaseOperationFailedException;
import com.qlik.assignment.exception.JWTMalformedException;

import java.util.List;

public interface UserService {

  User saveUser(User user) throws DatabaseOperationFailedException;

  Role saveRole(Role role) throws DatabaseOperationFailedException;

  void addRoleToUser(String userName, String roleName) throws DatabaseOperationFailedException;

  User getUser(String username) throws DatabaseOperationFailedException;

  List<User> getUsers() throws DatabaseOperationFailedException;

  String refreshTokenForUser(String authorizationToken) throws JWTMalformedException;
}
