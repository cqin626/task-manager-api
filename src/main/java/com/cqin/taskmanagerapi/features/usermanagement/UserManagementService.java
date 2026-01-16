package com.cqin.taskmanagerapi.features.usermanagement;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cqin.taskmanagerapi.common.exceptions.httpexceptions.ConflictException;
import com.cqin.taskmanagerapi.common.exceptions.httpexceptions.ResourceNotFoundException;
import com.cqin.taskmanagerapi.common.responses.SliceResponse;
import com.cqin.taskmanagerapi.common.responses.SliceResponseMapper;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.CreateUserRequest;
import com.cqin.taskmanagerapi.features.usermanagement.dtos.GetUserResponse;

@Service
public class UserManagementService {
   private UserManagementRepo userManagementRepo;
   private PasswordEncoder passwordEncoder;

   public UserManagementService(UserManagementRepo userManagementRepo, PasswordEncoder passwordEncoder) {
      this.userManagementRepo = userManagementRepo;
      this.passwordEncoder = passwordEncoder;
   }

   public SliceResponse<GetUserResponse> getUsers(int page, int size) {
      Pageable pageable = PageRequest.of(
            page,
            size,
            Sort
                  .by("id").descending());

      return SliceResponseMapper.from(
            this.userManagementRepo
                  .findAllBy(pageable)
                  .map(UserMapper::toDto));
   }

   public User getUserByEmail(String email) {
      User user = this.userManagementRepo.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Invalid user"));

      return user;
   }

   public GetUserResponse addUser(CreateUserRequest createUserReq) {
      User user = new User(
            createUserReq.email(),
            createUserReq.firstName(),
            createUserReq.lastName(),
            this.passwordEncoder.encode(createUserReq.password()));

      if (this.userManagementRepo.existsByEmail(user.getEmail()))
         throw new ConflictException("User already exists");

      User savedUser = this.userManagementRepo.save(user);

      return UserMapper.toDto(savedUser);
   }
}
