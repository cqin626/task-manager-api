package com.cqin.taskmanagerapi.features.authtokenmanagement;

import org.springframework.stereotype.Repository;

import com.cqin.taskmanagerapi.features.usermanagement.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AuthTokenRepo extends JpaRepository<RefreshToken, Long> {
   Optional<RefreshToken> findByTokenHash(String tokenHash);

   List<RefreshToken> findAllByUserAndRevokedFalse(User user);
}
