package com.cqin.taskmanagerapi.common.exceptions.httpexceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends HttpException {
   public ConflictException(String message) {
      super(HttpStatus.CONFLICT, message);
   }
}
