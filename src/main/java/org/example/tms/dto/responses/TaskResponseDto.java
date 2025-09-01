package org.example.tms.dto.responses;

import lombok.Builder;
import org.example.tms.model.enums.TaskPriority;
import org.example.tms.model.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record TaskResponseDto(UUID id,
                              String title,
                              String description,
                              TaskStatus status,
                              TaskPriority priority,
                              UserResponseDto author,
                              List<TaskAssigneeResponseDto> taskAssignees,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt,
                              List<CommentResponseDto> comments) {
}
