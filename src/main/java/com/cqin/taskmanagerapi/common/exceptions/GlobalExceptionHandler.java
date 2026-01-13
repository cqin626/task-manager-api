package com.cqin.taskmanagerapi.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.cqin.taskmanagerapi.common.exceptions.httpexceptions.HttpException;
import com.cqin.taskmanagerapi.common.responses.APIResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
   @ExceptionHandler(NoHandlerFoundException.class)
   public ResponseEntity<APIResponse<Void>> handleInvalidEndpoint(NoHandlerFoundException ex) {
      return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(APIResponse.error(ex.getMessage()));
   }

   @ExceptionHandler(HttpException.class)
   public ResponseEntity<APIResponse<Void>> handleHttpException(HttpException ex) {
      return ResponseEntity
            .status(ex.getStatus())
            .body(APIResponse.error(ex.getMessage()));
   }

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<APIResponse<Void>> handleInvalidRequestBody(MethodArgumentNotValidException ex) {
      String[] message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + " " + err.getDefaultMessage())
            .toArray(String[]::new);

      return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(APIResponse.error(message));
   }

   @ExceptionHandler(Exception.class)
   public ResponseEntity<APIResponse<Void>> handleInternalServerError(Exception ex) {
      // TODO: Add logging
      return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(APIResponse.error("Internal server error"));
   }
}
