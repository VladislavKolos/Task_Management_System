package org.example.tms.dto.requests.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms.validator.constraint.annotation.TaskExists;
import org.example.tms.validator.constraint.annotation.UserExists;
import org.example.tms.validator.constraint.annotation.ValidTimestamp;

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
