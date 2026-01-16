package com.cqin.taskmanagerapi.features.taskmanagement;

import com.cqin.taskmanagerapi.features.taskmanagement.dtos.GetTaskResponse;

public class TaskMapper {
   public static GetTaskResponse toDto(Task task) {
      return new GetTaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getCreatedAt());
   }
}
