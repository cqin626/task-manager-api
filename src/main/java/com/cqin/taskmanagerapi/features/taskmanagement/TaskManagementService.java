package com.cqin.taskmanagerapi.features.taskmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqin.taskmanagerapi.common.exceptions.httpexceptions.BadRequestException;
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
   private static final Set<String> ALLOWED_SORTS = Set.of("createdAt", "title");

   public TaskManagementService(TaskManagementRepo taskManagementRepo, EntityManager entityManager) {
      this.taskManagementRepo = taskManagementRepo;
      this.entityManager = entityManager;
   }

   public SliceResponse<GetTaskResponse> getTasks(long uid, int page, int size, TaskStatus status, String sort) {
      User userRef = entityManager.getReference(User.class, uid);

      Sort userSort = this.getParsedSort(sort);
      
      Sort finalizedSort = userSort.and(Sort.by(Sort.Direction.DESC, "id"));

      Pageable pageable = PageRequest.of(page, size, finalizedSort);

      return SliceResponseMapper.from(
            this.taskManagementRepo
                  .findAllByUserAndStatusOptional(userRef, status, pageable)
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

   private Sort getParsedSort(String sortParam) {
      if (sortParam == null || sortParam.isBlank()) {
         return Sort.by(Sort.Direction.DESC, "createdAt");
      }

      List<Sort.Order> orders = new ArrayList<>();
      String[] parts = sortParam.split(",");

      for (String part : parts) {
         String field = part;
         Sort.Direction direction = Sort.Direction.ASC;

         if (part.startsWith("-")) {
            field = part.substring(1);
            direction = Sort.Direction.DESC;
         }

         if (!ALLOWED_SORTS.contains(field)) {
            throw new BadRequestException("Invalid sort");
         }

         orders.add(new Sort.Order(direction, field));
      }

      return Sort.by(orders);
   }
}