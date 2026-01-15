package com.cqin.taskmanagerapi.common.exceptions.httpexceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends HttpException {
   public ForbiddenException(String message) {
      super(HttpStatus.FORBIDDEN, message);
   }
}
