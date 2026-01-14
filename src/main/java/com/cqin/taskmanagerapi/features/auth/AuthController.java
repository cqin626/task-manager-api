package com.cqin.taskmanagerapi.features.auth;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cqin.taskmanagerapi.common.responses.APIResponse;
import com.cqin.taskmanagerapi.features.auth.dtos.LoginUserRequest;
import com.cqin.taskmanagerapi.features.authtokenmanagement.AuthTokenService;
import com.cqin.taskmanagerapi.features.authtokenmanagement.dtos.TokenResponse;
import com.cqin.taskmanagerapi.features.usermanagement.User;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.CreateUserRequest;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.GetUserResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
   private final AuthService authService;
   private final AuthTokenService authTokenService;

   public AuthController(AuthService authService, AuthTokenService authTokenService) {
      this.authService = authService;
      this.authTokenService = authTokenService;
   }

   @PostMapping("/registration")
   public ResponseEntity<APIResponse<GetUserResponse>> registerUser(
         @RequestBody @Valid CreateUserRequest createUserReq) {
      GetUserResponse createUserRes = this.authService.registerUser(createUserReq);

      return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success(createUserRes));
   }

   @PostMapping("/login")
   public ResponseEntity<APIResponse<TokenResponse>> handlerUserLogin(
         @RequestBody @Valid LoginUserRequest loginUserReq) {
      User verifiedUser = this.authService.getVerifiedUser(loginUserReq);

      Map<String, Object> accessClaims = Map.of("id", verifiedUser.getId(), "email", verifiedUser.getEmail());
      Map<String, Object> refreshClaims = Map.of("id", verifiedUser.getId());

      TokenResponse accessToken = this.authTokenService.generateAccessToken(accessClaims);
      TokenResponse refreshToken = this.authTokenService.generateRefreshToken(refreshClaims, verifiedUser);

      ResponseCookie refreshCookie = ResponseCookie
            .from("refresh_token", refreshToken.token())
            .httpOnly(true)
            .secure(true)
            .sameSite("Lax")
            .path("/api/v1/auth")
            .maxAge(Duration.between(Instant.now(), refreshToken.expiresAt()))
            .build();

      return ResponseEntity
            .status(HttpStatus.OK)
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .body(APIResponse.success(accessToken));
   }

   @PostMapping("/logout")
   public ResponseEntity<APIResponse<Void>> handleUserLogout(
         @CookieValue(name = "refresh_token", required = false) String refreshToken) {
      if (refreshToken != null) {
         this.authTokenService.invalidateRefreshToken(refreshToken);
      }

      ResponseCookie refreshCookie = ResponseCookie
            .from("refresh_token", "")
            .httpOnly(true)
            .secure(true)
            .sameSite("Lax")
            .path("/api/v1/auth")
            .maxAge(0)
            .build();

      return ResponseEntity
            .status(HttpStatus.OK)
            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
            .body(APIResponse.success(null));
   }

}
