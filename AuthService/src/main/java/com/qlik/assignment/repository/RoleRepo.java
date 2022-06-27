package com.qlik.assignment.repository;

import com.qlik.assignment.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
  Role findByName(String name);
}
