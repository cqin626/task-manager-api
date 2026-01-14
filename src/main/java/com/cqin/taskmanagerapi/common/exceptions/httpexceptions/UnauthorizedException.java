package com.cqin.taskmanagerapi.common.exceptions.httpexceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends HttpException {
   public UnauthorizedException(String message) {
      super(HttpStatus.UNAUTHORIZED, message);
   }
}
