package org.example.tms.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssigneeResponseDto {
    private UUID id;
    private TaskResponseDto task;
    private UserResponseDto assignee;
    private LocalDateTime assignedAt;
}
