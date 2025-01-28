package org.example.tms.dto.responses;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TaskAssigneeResponseDto(UUID id, TaskResponseDto task, UserResponseDto assignee,
                                      LocalDateTime assignedAt) {
}
