package com.cqin.taskmanagerapi.features.usermanagement;

import java.util.List;

import org.springframework.stereotype.Service;

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
      return this.userManagementRepo.save(user);
   }
}
