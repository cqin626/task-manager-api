package com.cqin.taskmanagerapi.features.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cqin.taskmanagerapi.common.responses.APIResponse;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.CreateUserRequest;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.GetUserResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
   private final AuthService authService;

   public AuthController(AuthService authService) {
      this.authService = authService;
   }

   @PostMapping("/registration")
   public ResponseEntity<APIResponse<GetUserResponse>> registerUser(
         @RequestBody @Valid CreateUserRequest createUserReq) {
      GetUserResponse createUserRes = this.authService.registerUser(createUserReq);

      return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success(createUserRes));
   }
}
