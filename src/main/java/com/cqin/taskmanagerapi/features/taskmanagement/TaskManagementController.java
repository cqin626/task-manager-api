package com.cqin.taskmanagerapi.features.taskmanagement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cqin.taskmanagerapi.common.responses.APIResponse;
import com.cqin.taskmanagerapi.features.taskmanagement.dtos.CreateTaskRequest;
import com.cqin.taskmanagerapi.features.taskmanagement.dtos.GetTaskResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskManagementController {

   private final TaskManagementService taskManagementService;

   public TaskManagementController(TaskManagementService taskManagementService) {
      this.taskManagementService = taskManagementService;
   }

   @GetMapping("")
   public ResponseEntity<APIResponse<List<GetTaskResponse>>> getUserTasks(@AuthenticationPrincipal long uid) {
      List<GetTaskResponse> userTasks = this.taskManagementService.getUserTasks(uid);

      return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(userTasks));
   }

   @PostMapping("")
   public ResponseEntity<APIResponse<GetTaskResponse>> addTask(
         @RequestBody @Valid CreateTaskRequest createTaskReq,
         @AuthenticationPrincipal long uid) {
      GetTaskResponse createTaskRes = this.taskManagementService.addTask(createTaskReq, uid);

      return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success(createTaskRes));
   }

}
