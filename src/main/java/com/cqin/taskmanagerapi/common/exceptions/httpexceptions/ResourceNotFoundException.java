package com.cqin.taskmanagerapi.common.exceptions.httpexceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends HttpException {
   public ResourceNotFoundException(String message) {
      super(HttpStatus.NOT_FOUND, message);
   }
}
