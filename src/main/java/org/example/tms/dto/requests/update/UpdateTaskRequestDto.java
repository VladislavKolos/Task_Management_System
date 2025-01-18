package org.example.tms.dto.requests.update;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms.annotation.custom.UserExists;
import org.example.tms.annotation.custom.ValidTimestamp;
import org.example.tms.model.enums.TaskPriority;
import org.example.tms.model.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequestDto {

    @Size(max = 255)
    private String title;

    @Size(max = 500)
    private String description;
    private TaskStatus status;
    private TaskPriority priority;

    @UserExists
    private UUID authorId;

    @ValidTimestamp(message = "CreatedAt timestamp cannot be in the future")
    private LocalDateTime createdAt;

    @ValidTimestamp(message = "UpdatedAt timestamp cannot be in the future")
    private LocalDateTime updatedAt;
}
