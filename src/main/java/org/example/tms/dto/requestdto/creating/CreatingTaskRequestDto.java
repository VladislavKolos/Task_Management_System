package org.example.tms.dto.requestdto.creating;

import jakarta.validation.constraints.NotNull;
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
public class CreatingTaskRequestDto {

    @Size(max = 255)
    @NotNull
    private String title;

    @Size(max = 500)
    private String description;

    @NotNull
    private TaskStatus status;

    @NotNull
    private TaskPriority priority;

    @NotNull
    private UUID authorId;
    private LocalDateTime createdAt;
}
