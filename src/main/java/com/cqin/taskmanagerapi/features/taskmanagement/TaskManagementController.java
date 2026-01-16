package com.cqin.taskmanagerapi.features.taskmanagement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cqin.taskmanagerapi.common.responses.APIResponse;
import com.cqin.taskmanagerapi.features.taskmanagement.dtos.CreateTaskRequest;
import com.cqin.taskmanagerapi.features.taskmanagement.dtos.GetTaskResponse;
import com.cqin.taskmanagerapi.features.taskmanagement.dtos.UpdateTaskRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskManagementController {

   private final TaskManagementService taskManagementService;

   public TaskManagementController(TaskManagementService taskManagementService) {
      this.taskManagementService = taskManagementService;
   }

   @GetMapping("")
   public ResponseEntity<APIResponse<List<GetTaskResponse>>> getTasks(@AuthenticationPrincipal long uid) {
      List<GetTaskResponse> userTasks = this.taskManagementService.getTasks(uid);

      return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(userTasks));
   }

   @GetMapping("/{taskId}")
   public ResponseEntity<APIResponse<GetTaskResponse>> getTask(
         @PathVariable long taskId,
         @AuthenticationPrincipal long uid) {
      GetTaskResponse userTask = this.taskManagementService.getTask(taskId, uid);

      return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(userTask));
   }

   @PostMapping("")
   public ResponseEntity<APIResponse<GetTaskResponse>> addTask(
         @RequestBody @Valid CreateTaskRequest createTaskReq,
         @AuthenticationPrincipal long uid) {
      GetTaskResponse createTaskRes = this.taskManagementService.addTask(createTaskReq, uid);

      return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success(createTaskRes));
   }

   @PutMapping("/{taskId}")
   public ResponseEntity<APIResponse<GetTaskResponse>> updateTask(
         @PathVariable long taskId,
         @AuthenticationPrincipal long uid,
         @RequestBody @Valid UpdateTaskRequest updateTaskReq) {
      GetTaskResponse updateTaskRes = this.taskManagementService.updateTask(updateTaskReq, taskId, uid);

      return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(updateTaskRes));
   }

   @DeleteMapping("/{taskId}")
   public ResponseEntity<APIResponse<Void>> deleteTask(
         @PathVariable long taskId,
         @AuthenticationPrincipal long uid) {
      this.taskManagementService.deleteTask(taskId, uid);

      return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(null));
   }

}
