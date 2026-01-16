package com.cqin.taskmanagerapi.features.taskmanagement;

import java.time.Instant;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqin.taskmanagerapi.common.exceptions.httpexceptions.ResourceNotFoundException;
import com.cqin.taskmanagerapi.common.responses.SliceResponse;
import com.cqin.taskmanagerapi.common.responses.SliceResponseMapper;
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

   public SliceResponse<GetTaskResponse> getTasks(long uid, int page, int size) {
      User userRef = entityManager.getReference(User.class, uid);

      Pageable pageable = PageRequest.of(
            page,
            size,
            Sort
                  .by("createdAt").descending()
                  .and(Sort.by("id").descending()));

      return SliceResponseMapper.from(
            this.taskManagementRepo
                  .findAllByUser(userRef, pageable)
                  .map(TaskMapper::toDto));
   }

   public GetTaskResponse getTask(long taskId, long uid) {
      Task task = this.taskManagementRepo.findByIdAndUserId(taskId, uid)
            .orElseThrow(() -> new ResourceNotFoundException("Cannot access task you do not own"));

      return TaskMapper.toDto(task);
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

   @Transactional
   public void deleteTask(long taskId, long uid) {
      long deleted = this.taskManagementRepo.deleteByIdAndUserId(taskId, uid);

      if (deleted == 0) {
         throw new ResourceNotFoundException("Cannot modify task you do not own");
      }
   }
}