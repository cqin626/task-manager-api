package com.cqin.taskmanagerapi.features.auth;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cqin.taskmanagerapi.common.exceptions.httpexceptions.UnauthorizedException;
import com.cqin.taskmanagerapi.common.responses.APIResponse;
import com.cqin.taskmanagerapi.features.auth.dtos.LoginUserRequest;
import com.cqin.taskmanagerapi.features.authtokenmanagement.dtos.TokenResponse;
import com.cqin.taskmanagerapi.features.usermanagement.User;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.CreateUserRequest;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.GetUserResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
      private final AuthService authService;

      @Value("${app.cookie.secure:true}")
      private boolean cookieSecure;

      public AuthController(AuthService authService) {
            this.authService = authService;
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
            TokenResponse accessToken = this.authService.getAccessToken(verifiedUser);
            TokenResponse refreshToken = this.authService.getRefreshToken(verifiedUser);

            ResponseCookie refreshTokenCookie = ResponseCookie
                        .from("refresh_token", refreshToken.token())
                        .httpOnly(true)
                        .secure(cookieSecure)
                        .sameSite("Lax")
                        .path("/api/v1/auth")
                        .maxAge(Duration.between(Instant.now(), refreshToken.expiresAt()))
                        .build();

            return ResponseEntity
                        .status(HttpStatus.OK)
                        .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                        .body(APIResponse.success(accessToken));
      }

      @PostMapping("/logout")
      public ResponseEntity<APIResponse<Void>> handleUserLogout(
                  @CookieValue(name = "refresh_token", required = false) String refreshTokenStr) {
            if (refreshTokenStr == null) {
                  return ResponseEntity
                              .status(HttpStatus.OK)
                              .body(APIResponse.success(null));
            }
            this.authService.invalidateRefreshToken(refreshTokenStr);

            ResponseCookie invalidatedRefreshTokenCookie = ResponseCookie
                        .from("refresh_token", "")
                        .httpOnly(true)
                        .secure(cookieSecure)
                        .sameSite("Lax")
                        .path("/api/v1/auth")
                        .maxAge(0)
                        .build();

            return ResponseEntity
                        .status(HttpStatus.OK)
                        .header(HttpHeaders.SET_COOKIE, invalidatedRefreshTokenCookie.toString())
                        .body(APIResponse.success(null));
      }

      @PostMapping("/refresh")
      public ResponseEntity<APIResponse<TokenResponse>> handleRefresh(
                  @CookieValue(name = "refresh_token", required = false) String refreshTokenStr) {
            if (refreshTokenStr == null) {
                  throw new UnauthorizedException("Unauthorized to refresh");
            }

            User user = this.authService.getUserFromVerifiedRefreshToken(refreshTokenStr);
            TokenResponse accessToken = this.authService.getAccessToken(user);
            TokenResponse refreshToken = this.authService.getRefreshToken(user);

            ResponseCookie refreshTokenCookie = ResponseCookie
                        .from("refresh_token", refreshToken.token())
                        .httpOnly(true)
                        .secure(cookieSecure)
                        .sameSite("Lax")
                        .path("/api/v1/auth")
                        .maxAge(Duration.between(Instant.now(), refreshToken.expiresAt()))
                        .build();

            return ResponseEntity
                        .status(HttpStatus.OK)
                        .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                        .body(APIResponse.success(accessToken));
      }
}
