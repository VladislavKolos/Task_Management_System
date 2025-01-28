package org.example.tms.exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApiExceptionDto(Integer status,
                              String message,
                              String error,
                              String path,
                              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timestamp) {
}
