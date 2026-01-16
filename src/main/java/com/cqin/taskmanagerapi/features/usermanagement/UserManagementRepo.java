package com.cqin.taskmanagerapi.features.usermanagement;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserManagementRepo extends JpaRepository<User, Long> {
   boolean existsByEmail(String email);

   Slice<User> findAllBy(Pageable pageable);

   Optional<User> findByEmail(String email);
}
