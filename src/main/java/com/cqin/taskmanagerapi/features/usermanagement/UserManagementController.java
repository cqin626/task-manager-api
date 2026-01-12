package com.cqin.taskmanagerapi.features.usermanagement;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserManagementController {
   private UserManagementService userManagementService;

   public UserManagementController(UserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   @GetMapping()
   public ResponseEntity<List<User>> getUsers() {
      var users = this.userManagementService.getUsers();
      return new ResponseEntity<>(users, HttpStatus.OK);
   }

   @PostMapping()
   public ResponseEntity<User> addUser(@RequestBody User user) {
      User savedUser = userManagementService.addUser(user);
      return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
   }
}
