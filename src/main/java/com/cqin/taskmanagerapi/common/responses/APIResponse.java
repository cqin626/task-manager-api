package com.cqin.taskmanagerapi.common.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record APIResponse<T>(boolean success, String[] message, T data) {
   public static <T> APIResponse<T> success(T data) {
      return new APIResponse<>(true, null, data);
   }

   public static <T> APIResponse<T> error(String... messages) {
      return new APIResponse<>(false, messages, null);
   }
}