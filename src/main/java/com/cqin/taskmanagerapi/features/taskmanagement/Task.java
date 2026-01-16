package com.cqin.taskmanagerapi.features.taskmanagement;

import java.time.Instant;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.cqin.taskmanagerapi.features.usermanagement.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "task")
public class Task {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

   @Column(nullable = false)
   private String title;

   private String description;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private TaskStatus status;

   @Column(nullable = false)
   private Instant createdAt;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id", nullable = false)
   @OnDelete(action = OnDeleteAction.CASCADE)
   private User user;

   protected Task() {
   }

   public Task(String title, String description, Instant createdAt, User user) {
      this.title = title;
      this.description = description;
      this.status = TaskStatus.PENDING;
      this.createdAt = createdAt;
      this.user = user;
   }

   public long getId() {
      return id;
   }

   public String getTitle() {
      return title;
   }

   public String getDescription() {
      return description;
   }

   public TaskStatus getStatus() {
      return status;
   }

   public Instant getCreatedAt() {
      return createdAt;
   }

   public User getUser() {
      return user;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public void setStatus(TaskStatus status) {
      this.status = status;
   }

   public void setUser(User user) {
      this.user = user;
   }

}
