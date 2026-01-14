package com.cqin.taskmanagerapi.features.authtokenmanagement;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AuthTokenRepo extends JpaRepository<RefreshToken, Long> {
   Optional<RefreshToken> findByTokenHash(String tokenHash);
}
