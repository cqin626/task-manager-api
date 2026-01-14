package com.cqin.taskmanagerapi.features.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cqin.taskmanagerapi.common.exceptions.httpexceptions.ResourceNotFoundException;
import com.cqin.taskmanagerapi.common.exceptions.httpexceptions.UnauthorizedException;
import com.cqin.taskmanagerapi.features.auth.dtos.LoginUserRequest;
import com.cqin.taskmanagerapi.features.usermanagement.User;
import com.cqin.taskmanagerapi.features.usermanagement.UserManagementService;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.CreateUserRequest;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.GetUserResponse;

@Service
public class AuthService {
   private final UserManagementService userManagementService;
   private PasswordEncoder passwordEncoder;

   public AuthService(UserManagementService userManagementService, PasswordEncoder passwordEncoder) {
      this.userManagementService = userManagementService;
      this.passwordEncoder = passwordEncoder;
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
}
