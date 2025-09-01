package org.example.tms.dto.responses;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CommentResponseDto(UUID id, String content, TaskResponseDto task, UserResponseDto author) {
}
