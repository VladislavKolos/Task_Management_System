package org.example.tms.dto.requests.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms.annotation.custom.TaskExists;
import org.example.tms.annotation.custom.UserExists;
import org.example.tms.annotation.custom.ValidTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskAssigneeRequestDto {

    @NotNull
    @TaskExists
    private UUID taskId;

    @NotNull
    @UserExists
    private UUID assigneeId;

    @ValidTimestamp(message = "AssignedAt timestamp cannot be in the future")
    private LocalDateTime assignedAt;
}
