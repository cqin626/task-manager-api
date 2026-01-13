package com.cqin.taskmanagerapi.features.usermanagement;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cqin.taskmanagerapi.common.exceptions.httpexceptions.ConflictException;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.CreateUserRequest;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.GetUserResponse;

@Service
public class UserManagementService {
   private UserManagementRepo userManagementRepo;

   public UserManagementService(UserManagementRepo userManagementRepo) {
      this.userManagementRepo = userManagementRepo;
   }

   public List<GetUserResponse> getUsers() {
      List<GetUserResponse> users = this.userManagementRepo.findAll().stream()
            .map(user -> new GetUserResponse(user.getId(), user.getEmail())).toList();
      return users;
   }

   public GetUserResponse addUser(CreateUserRequest createUserReq) {
      User user = new User(createUserReq.email(), createUserReq.password());

      if (this.userManagementRepo.existsByEmail(user.getEmail()))
         throw new ConflictException("User already exists");

      User savedUser = this.userManagementRepo.save(user);

      return new GetUserResponse(savedUser.getId(), savedUser.getEmail());
   }
}
