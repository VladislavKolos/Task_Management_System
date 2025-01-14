package org.example.tms.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms.model.Comment;
import org.example.tms.model.TaskAssignee;
import org.example.tms.model.enums.TaskPriority;
import org.example.tms.model.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto {
    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private UserResponseDto author;
    private List<TaskAssignee> taskAssignees;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Comment> comments;
}
