package com.cqin.taskmanagerapi.common.exceptions.httpexceptions;

import org.springframework.http.HttpStatus;

public abstract class HttpException extends RuntimeException {
   private final HttpStatus status;

   public HttpException(HttpStatus status, String message) {
      super(message);
      this.status = status;
   }

   public HttpStatus getStatus() {
      return status;
   }
}