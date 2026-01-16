package com.cqin.taskmanagerapi.common.responses;

import org.springframework.data.domain.Slice;

public class SliceResponseMapper {
   public static <T> SliceResponse<T> toSliceResponse(Slice<T> slice) {
      return new SliceResponse<>(
            slice.getContent(),
            slice.getNumber(),
            slice.getSize(),
            slice.hasNext());
   }
}
