package com.qlik.assignment.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qlik.assignment.domain.Role;
import com.qlik.assignment.domain.User;
import com.qlik.assignment.exception.DatabaseOperationFailedException;
import com.qlik.assignment.exception.JWTMalformedException;
import com.qlik.assignment.repository.UserRepo;
import com.qlik.assignment.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImp implements UserService, UserDetailsService {
  private final UserRepo userRepo;
  private final RoleRepo RoleRepo;
  private final PasswordEncoder passwordEncoder;

  @Override
  public User saveUser(User user) throws DatabaseOperationFailedException {
    try {
      log.info("Saving new user {} to the database", user.getUsername());
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      return userRepo.save(user);
    } catch (Exception e) {
      throw new DatabaseOperationFailedException(e.getMessage());
    }
  }

  @Override
  public Role saveRole(Role role) throws DatabaseOperationFailedException {
    try {
      log.info("Saving new role {} to the database", role.getName());
      return RoleRepo.save(role);
    } catch (Exception e) {
      throw new DatabaseOperationFailedException(e.getMessage());
    }
  }

  @Override
  public void addRoleToUser(String username, String roleName)
      throws DatabaseOperationFailedException {
    try {
      log.info("Adding new role {} to user {} to the database", roleName, username);
      User user = userRepo.findByUsername(username);
      Role role = RoleRepo.findByName(roleName);
      user.getRoles().add(role);
    } catch (Exception e) {
      throw new DatabaseOperationFailedException(e.getMessage());
    }
  }

  @Override
  public User getUser(String userName) throws DatabaseOperationFailedException {
    try {
      log.info("Fetching user {} ", userName);
      return userRepo.findByUsername(userName);
    } catch (Exception e) {
      throw new DatabaseOperationFailedException(e.getMessage());
    }
  }

  @Override
  public List<User> getUsers() throws DatabaseOperationFailedException {
    try {
      log.info("Fetching all users ");
      return userRepo.findAll();
    } catch (Exception e) {
      throw new DatabaseOperationFailedException(e.getMessage());
    }
  }

  @Override
  public String refreshTokenForUser(String authorizationToken) throws JWTMalformedException {
    if (authorizationToken != null && authorizationToken.startsWith("Bearer ")) {
      try {
        String refresh_token = authorizationToken.substring("Bearer ".length());
        Algorithm algorithm =
            Algorithm.HMAC256(
                "secret"
                    .getBytes(
                        StandardCharsets
                            .UTF_8)); // Need to load this secrets value from Vault or GCP secret
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refresh_token);
        String username = decodedJWT.getSubject();
        User user = this.getUser(username);
        return JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10mins
            .withClaim(
                "roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
            .sign(algorithm);

      } catch (Exception e) {
        throw new JWTMalformedException(e.getMessage());
      }
    } else {
      throw new JWTMalformedException("Invalid JWT token.");
    }
  }

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    User user = userRepo.findByUsername(userName);
    if (user == null) {
      log.error("User not found");
      throw new UsernameNotFoundException("User not found");
    } else {
      log.info("User found : {}", userName);
    }
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    user.getRoles()
        .forEach(
            role -> {
              authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), user.getPassword(), authorities);
  }
}
