package com.cqin.taskmanagerapi.features.authtokenmanagement.dtos;

import java.time.Instant;

public record TokenResponse(String token, Instant expiresAt) {
}
