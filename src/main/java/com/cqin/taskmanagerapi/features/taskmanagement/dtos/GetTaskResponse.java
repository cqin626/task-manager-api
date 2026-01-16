package com.cqin.taskmanagerapi.features.taskmanagement.dtos;

import java.time.Instant;

import com.cqin.taskmanagerapi.features.taskmanagement.TaskStatus;

public record GetTaskResponse(
      long id,
      String title,
      String description,
      TaskStatus status,
      Instant createdAt) {
}
