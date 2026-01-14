package com.cqin.taskmanagerapi.features.usermanagement;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserManagementRepo extends JpaRepository<User, Long> {
   boolean existsByEmail(String email);

   Optional<User> findByEmail(String email);
}
