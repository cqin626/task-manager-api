package com.cqin.taskmanagerapi.features.taskmanagement;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cqin.taskmanagerapi.features.taskmanagement.dtos.CreateTaskRequest;
import com.cqin.taskmanagerapi.features.taskmanagement.dtos.GetTaskResponse;
import com.cqin.taskmanagerapi.features.usermanagement.User;

import jakarta.persistence.EntityManager;

@Service
public class TaskManagementService {

   private final TaskManagementRepo taskManagementRepo;
   private final EntityManager entityManager;

   public TaskManagementService(TaskManagementRepo taskManagementRepo, EntityManager entityManager) {
      this.taskManagementRepo = taskManagementRepo;
      this.entityManager = entityManager;
   }

   public List<GetTaskResponse> getUserTasks(long uid) {
      User userRef = entityManager.getReference(User.class, uid);

      List<GetTaskResponse> userTasks = this.taskManagementRepo.findAllByUser(userRef).stream()
            .map(userTask -> new GetTaskResponse(
                  userTask.getId(),
                  userTask.getTitle(),
                  userTask.getDescription(),
                  userTask.getStatus(),
                  userTask.getCreatedAt()))
            .toList();
      return userTasks;
   }

   public GetTaskResponse addTask(CreateTaskRequest createTaskReq, long uid) {
      User userRef = entityManager.getReference(User.class, uid);

      Task savedTask = this.taskManagementRepo.save(new Task(
            createTaskReq.title(),
            createTaskReq.description(),
            Instant.now(),
            userRef));

      return new GetTaskResponse(
            savedTask.getId(),
            savedTask.getTitle(),
            savedTask.getDescription(),
            savedTask.getStatus(),
            savedTask.getCreatedAt());
   }
}