package com.cqin.taskmanagerapi.features.usermanagement.dtos;

import com.cqin.taskmanagerapi.features.usermanagement.UserRole;

public record GetUserResponse(long id, String email, UserRole role) {
}
