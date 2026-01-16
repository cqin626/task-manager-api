package com.cqin.taskmanagerapi.features.taskmanagement.dtos;

import com.cqin.taskmanagerapi.features.taskmanagement.TaskStatus;

import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(
      @Size(min = 1, max = 255) String title,
      @Size(max = 1000) String description,
      TaskStatus status) {
}
