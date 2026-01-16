package com.cqin.taskmanagerapi.features.taskmanagement.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskRequest(
      @NotBlank String title,
      String description) {
}
