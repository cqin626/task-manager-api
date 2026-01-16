package com.cqin.taskmanagerapi.features.taskmanagement.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
      @NotBlank @Size(max = 255) String title,
      @Size(max = 1000) String description) {
}
