package com.cqin.taskmanagerapi.features.usermanagement;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cqin.taskmanagerapi.common.exceptions.httpexceptions.ConflictException;

@Service
public class UserManagementService {
   private UserManagementRepo userManagementRepo;

   public UserManagementService(UserManagementRepo userManagementRepo) {
      this.userManagementRepo = userManagementRepo;
   }

   public List<User> getUsers() {
      return this.userManagementRepo.findAll();
   }

   public User addUser(User user) {
      if (this.userManagementRepo.existsByEmail(user.getEmail()))
         throw new ConflictException("User already exists");
      return this.userManagementRepo.save(user);
   }
}
