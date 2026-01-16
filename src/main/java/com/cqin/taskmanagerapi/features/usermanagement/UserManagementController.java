package com.cqin.taskmanagerapi.features.usermanagement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cqin.taskmanagerapi.common.responses.APIResponse;
import com.cqin.taskmanagerapi.common.responses.SliceResponse;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.GetUserResponse;

import jakarta.validation.constraints.Max;

@RestController
@Validated
@RequestMapping("/api/v1/users")
public class UserManagementController {
   private UserManagementService userManagementService;

   public UserManagementController(UserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   @GetMapping
   public ResponseEntity<APIResponse<SliceResponse<GetUserResponse>>> getUsers(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "50") @Max(50) int size) {
      SliceResponse<GetUserResponse> users = userManagementService.getUsers(page, size);

      return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(users));
   }
}
