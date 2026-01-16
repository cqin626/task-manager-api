package com.cqin.taskmanagerapi.features.usermanagement;

import com.cqin.taskmanagerapi.features.usermanagement.dtos.GetUserResponse;

public class UserMapper {
   public static GetUserResponse toDto(User user) {
      return new GetUserResponse(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getRole());
   }
}
