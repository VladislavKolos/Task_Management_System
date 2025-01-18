package org.example.tms.dto.requests.update;

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
public class UpdateTaskAssigneeRequestDto {
    private UUID taskId;
    private UUID assigneeId;
    private LocalDateTime assignedAt;
}
