package org.example.tms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.tms.model.enums.TaskPriority;
import org.example.tms.model.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Task extends BaseEntity {

    @Size(max = 255)
    @Column(name = "title", nullable = false)
    @NotNull
    private String title;

    @Size(max = 500)
    @Column(name = "description")
    private String description;

    @Column(name = "status", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "priority", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "task")
    private List<TaskAssignee> taskAssignees = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "task")
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void updateTimestamps() {
        updatedAt = LocalDateTime.now();
    }
}
