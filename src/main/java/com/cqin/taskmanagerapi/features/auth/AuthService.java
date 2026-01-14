package com.cqin.taskmanagerapi.features.auth;

import org.springframework.stereotype.Service;

import com.cqin.taskmanagerapi.features.usermanagement.UserManagementService;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.CreateUserRequest;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.GetUserResponse;

@Service
public class AuthService {
   private final UserManagementService userManagementService;

   public AuthService(UserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   public GetUserResponse registerUser(CreateUserRequest createUserReq) {
      return this.userManagementService.addUser(createUserReq);
   }
}
