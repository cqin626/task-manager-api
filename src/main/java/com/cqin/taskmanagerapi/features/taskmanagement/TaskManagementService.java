package com.cqin.taskmanagerapi.features.taskmanagement;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqin.taskmanagerapi.common.exceptions.httpexceptions.ResourceNotFoundException;
import com.cqin.taskmanagerapi.features.taskmanagement.dtos.CreateTaskRequest;
import com.cqin.taskmanagerapi.features.taskmanagement.dtos.GetTaskResponse;
import com.cqin.taskmanagerapi.features.taskmanagement.dtos.UpdateTaskRequest;
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

   public List<GetTaskResponse> getTasks(long uid) {
      User userRef = entityManager.getReference(User.class, uid);

      List<GetTaskResponse> userTasks = this.taskManagementRepo.findAllByUser(userRef).stream()
            .map(userTask -> TaskMapper.toDto(userTask))
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

      return TaskMapper.toDto(savedTask);
   }

   @Transactional
   public GetTaskResponse updateTask(UpdateTaskRequest updateTaskReq, long taskId, long uid) {
      Task task = this.taskManagementRepo.findByIdAndUserId(taskId, uid)
            .orElseThrow(() -> new ResourceNotFoundException("Cannot modify task you do not own"));

      if (updateTaskReq.title() != null)
         task.setTitle(updateTaskReq.title());

      if (updateTaskReq.description() != null)
         task.setDescription(updateTaskReq.description());

      if (updateTaskReq.status() != null)
         task.setStatus(updateTaskReq.status());

      return TaskMapper.toDto(task);
   }
}