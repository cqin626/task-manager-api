package com.cqin.taskmanagerapi.features.auth;

import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cqin.taskmanagerapi.common.exceptions.httpexceptions.ForbiddenException;
import com.cqin.taskmanagerapi.common.exceptions.httpexceptions.ResourceNotFoundException;
import com.cqin.taskmanagerapi.common.exceptions.httpexceptions.UnauthorizedException;
import com.cqin.taskmanagerapi.features.auth.dtos.LoginUserRequest;
import com.cqin.taskmanagerapi.features.authtokenmanagement.AuthTokenService;
import com.cqin.taskmanagerapi.features.authtokenmanagement.RefreshToken;
import com.cqin.taskmanagerapi.features.authtokenmanagement.dtos.TokenResponse;
import com.cqin.taskmanagerapi.features.usermanagement.User;
import com.cqin.taskmanagerapi.features.usermanagement.UserManagementService;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.CreateUserRequest;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.GetUserResponse;

import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
   private PasswordEncoder passwordEncoder;
   private final AuthTokenService authTokenService;
   private final UserManagementService userManagementService;

   public AuthService(
         UserManagementService userManagementService,
         PasswordEncoder passwordEncoder,
         AuthTokenService authTokenService) {
      this.passwordEncoder = passwordEncoder;
      this.authTokenService = authTokenService;
      this.userManagementService = userManagementService;
   }

   public GetUserResponse registerUser(CreateUserRequest createUserReq) {
      return this.userManagementService.addUser(createUserReq);
   }

   public User getVerifiedUser(LoginUserRequest loginUserReq) {
      User user;

      try {
         user = this.userManagementService.getUserByEmail(loginUserReq.email());
      } catch (ResourceNotFoundException e) {
         throw new UnauthorizedException("Invalid credentials");
      }

      if (!this.passwordEncoder.matches(loginUserReq.password(), user.getPassword())) {
         throw new UnauthorizedException("Invalid credentials");
      }

      return user;
   }

   public TokenResponse getAccessToken(User user) {
      Map<String, Object> accessClaims = Map.of(
            "uid", user.getId(),
            "email", user.getEmail(),
            "role", user.getRole());

      return this.authTokenService.generateAccessToken(accessClaims);
   }

   public TokenResponse getRefreshToken(User user) {
      Map<String, Object> refreshClaims = Map.of("uid", user.getId());

      return this.authTokenService.generateRefreshToken(refreshClaims, user);
   }

   public void invalidateRefreshToken(String refreshTokenStr) {
      this.authTokenService.invalidateRefreshToken(refreshTokenStr);
   }

   @Transactional
   public User getUserFromVerifiedRefreshToken(String refreshTokenStr) {
      this.authTokenService.getVerifiedAndParsedToken(refreshTokenStr);

      RefreshToken verifiedRefreshToken = this.authTokenService.getRefreshToken(refreshTokenStr);

      User user = verifiedRefreshToken.getUser();

      if (verifiedRefreshToken.isRevoked()) {
         // Anomaly detected, invalidate all tokens from the user in db
         this.authTokenService.invalidateAllTokensForUser(user);
         throw new ForbiddenException("Revoked token is used. Malicious activity is suspected.");
      }

      this.invalidateRefreshToken(refreshTokenStr);

      return user;
   }
}
