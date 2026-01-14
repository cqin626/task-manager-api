package com.cqin.taskmanagerapi.features.authtokenmanagement;

import java.time.Instant;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.cqin.taskmanagerapi.features.usermanagement.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false, unique = true)
   private String tokenHash;

   private boolean revoked;

   @Column(nullable = false)
   private Instant createdAt;

   @Column(nullable = false)
   private Instant expiresAt;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id", nullable = false)
   @OnDelete(action = OnDeleteAction.CASCADE)
   private User user;

   protected RefreshToken() {
   }

   public RefreshToken(String tokenHash, Instant createdAt, Instant expiresAt, User user) {
      this.revoked = false;
      this.tokenHash = tokenHash;
      this.createdAt = createdAt;
      this.expiresAt = expiresAt;
      this.user = user;
   }

   public boolean isRevoked() {
      return revoked;
   }

   public Instant getCreatedAt() {
      return createdAt;
   }

   public Instant getExpiresAt() {
      return expiresAt;
   }

   public User getUser() {
      return user;
   }

   public void setRevoked(boolean revoked) {
      this.revoked = revoked;
   }

   public void setCreatedAt(Instant createdAt) {
      this.createdAt = createdAt;
   }

   public void setExpiresAt(Instant expiresAt) {
      this.expiresAt = expiresAt;
   }

   public void setUser(User user) {
      this.user = user;
   }
}
