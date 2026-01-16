package com.cqin.taskmanagerapi.common.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.cqin.taskmanagerapi.common.exceptions.httpexceptions.HttpException;
import com.cqin.taskmanagerapi.common.responses.APIResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
      private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

      @ExceptionHandler(NoHandlerFoundException.class)
      public ResponseEntity<APIResponse<Void>> handleInvalidEndpoint(NoHandlerFoundException ex) {
            return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(APIResponse.error(ex.getMessage()));
      }

      @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
      public ResponseEntity<APIResponse<Void>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
            return ResponseEntity
                        .status(HttpStatus.METHOD_NOT_ALLOWED)
                        .body(APIResponse.error("Request method " + ex.getMethod() + " is not supported"));
      }

      @ExceptionHandler(HttpMessageNotReadableException.class)
      public ResponseEntity<APIResponse<Void>> handleInvalidJson(HttpMessageNotReadableException ex) {
            return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(APIResponse.error("Malformed JSON request"));
      }

      @ExceptionHandler(MethodArgumentTypeMismatchException.class)
      public ResponseEntity<APIResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
            return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(APIResponse.error("Invalid path variable: " + ex.getValue()));
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
            logger.error("Internal server error occurred", ex);
            return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(APIResponse.error("Internal server error"));
      }
}
