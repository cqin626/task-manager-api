package com.cqin.taskmanagerapi.features.usermanagement.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
      @NotBlank @Email String email,
      @NotNull @Size(min = 14, message = "password must be at least 14 characters") String password) {
}