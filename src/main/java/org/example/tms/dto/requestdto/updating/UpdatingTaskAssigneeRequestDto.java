package org.example.tms.dto.requestdto.updating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatingTaskAssigneeRequestDto {
    private UUID taskId;
    private UUID assigneeId;
    private LocalDateTime assignedAt;
}
