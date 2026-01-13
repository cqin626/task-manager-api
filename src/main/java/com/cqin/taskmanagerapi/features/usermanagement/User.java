package com.cqin.taskmanagerapi.features.usermanagement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false, unique = true)
   private String email;

   @Column(nullable = false)
   private String password;

   protected User() {
   }

   public User(String email, String password) {
      this.email = email;
      this.password = password;
   }

   public Long getId() {
      return id;
   }

   public String getEmail() {
      return email;
   }

   public String getPassword() {
      return password;
   }

   public void setEmail(String email) {
      this.email = email;
   };
}
