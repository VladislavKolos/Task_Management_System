package org.example.tms.dto.requestdto.updating;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms.model.enums.TaskPriority;
import org.example.tms.model.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatingTaskRequestDto {

    @Size(max = 255)
    private String title;

    @Size(max = 500)
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private UUID authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
