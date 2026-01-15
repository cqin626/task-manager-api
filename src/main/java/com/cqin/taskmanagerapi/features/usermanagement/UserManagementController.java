package com.cqin.taskmanagerapi.features.usermanagement;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cqin.taskmanagerapi.common.responses.APIResponse;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.GetUserResponse;

@RestController
@RequestMapping("/api/v1/users")
public class UserManagementController {
   private UserManagementService userManagementService;

   public UserManagementController(UserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   @GetMapping
   public ResponseEntity<APIResponse<List<GetUserResponse>>> getUsers() {
      List<GetUserResponse> users = userManagementService.getUsers();

      return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(users));
   }
}
