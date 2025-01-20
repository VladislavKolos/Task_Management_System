package org.example.tms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.annotation.custom.TaskExists;
import org.example.tms.annotation.custom.UserExists;
import org.example.tms.dto.requests.create.CreateTaskRequestDto;
import org.example.tms.dto.requests.update.UpdateTaskRequestDto;
import org.example.tms.dto.responses.TaskResponseDto;
import org.example.tms.model.User;
import org.example.tms.service.TaskService;
import org.example.tms.util.CurrentUserUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Tag(name = "Tasks", description = "Endpoints for managing tasks")
@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @Operation(
            summary = "Get task by ID",
            description = "Fetches a task by its unique ID.",
            tags = {"Tasks"}
    )
    @Parameter(
            name = "id",
            description = "Unique identifier of the task",
            required = true,
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable @TaskExists UUID id) {
        TaskResponseDto response = taskService.getTaskById(id);
        log.info("Retrieved task with ID: {}", id);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get tasks by author ID",
            description = "Fetches all tasks created by a specific author.",
            tags = {"Tasks"}
    )
    @Parameter(
            name = "authorId",
            description = "Unique identifier of the author",
            required = true,
            example = "123e4567-e89b-12d3-a456-426614174001"
    )
    @Parameter(
            name = "pageable",
            description = "Pagination and sorting information",
            example = "{ \"page\": 0, \"size\": 10, \"sort\": \"createdAt,desc\" }"
    )
    @GetMapping("/author/{authorId}")
    public ResponseEntity<Page<TaskResponseDto>> getTasksByAuthor(
            @PathVariable @UserExists UUID authorId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TaskResponseDto> tasks = taskService.getTasksByAuthor(authorId, pageable);
        log.info("Retrieved tasks for Author with ID: {}", authorId);

        return ResponseEntity.ok(tasks);
    }

    @Operation(
            summary = "Get tasks by assignee ID",
            description = "Fetches all tasks assigned to a specific user.",
            tags = {"Tasks"}
    )
    @Parameter(
            name = "assigneeId",
            description = "Unique identifier of the assignee",
            required = true,
            example = "123e4567-e89b-12d3-a456-426614174002"
    )
    @Parameter(
            name = "pageable",
            description = "Pagination and sorting information",
            example = "{ \"page\": 0, \"size\": 10, \"sort\": \"createdAt,desc\" }"
    )
    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<Page<TaskResponseDto>> getTasksByAssignee(
            @PathVariable @UserExists UUID assigneeId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TaskResponseDto> tasks = taskService.getTasksByAssignee(assigneeId, pageable);
        log.info("Retrieved tasks for assignee with ID: {}", assigneeId);

        return ResponseEntity.ok(tasks);
    }

    @Operation(
            summary = "Get all tasks",
            description = "Fetches all tasks with pagination and sorting.",
            tags = {"Tasks"}
    )
    @Parameter(
            name = "pageable",
            description = "Pagination and sorting information",
            example = "{ \"page\": 0, \"size\": 10, \"sort\": \"createdAt,desc\" }"
    )
    @GetMapping
    public ResponseEntity<Page<TaskResponseDto>> getAllTasks(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TaskResponseDto> tasks = taskService.getAllTasks(pageable);
        log.info("Retrieved all tasks");

        return ResponseEntity.ok(tasks);
    }

    @Operation(
            summary = "Create a new task",
            description = "Creates a new task with the provided details.",
            tags = {"Tasks"}
    )
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody CreateTaskRequestDto request) {
        TaskResponseDto response = taskService.createTask(request);
        log.info("Task created successfully with ID: {}", response.getId());

        return ResponseEntity.created(URI.create("/api/tasks/" + response.getId()))
                .body(response);
    }

    @Operation(
            summary = "Update an existing task",
            description = "Updates the details of a task identified by its ID.",
            tags = {"Tasks"}
    )
    @Parameter(
            name = "id",
            description = "Unique identifier of the task to update",
            required = true,
            example = "123e4567-e89b-12d3-a456-426614174003"
    )
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable @TaskExists UUID id,
            @Valid @RequestBody UpdateTaskRequestDto request) {
        User currentUser = CurrentUserUtil.getCurrentUser();

        TaskResponseDto response = taskService.updateTask(id, request, currentUser);
        log.info("Task with ID: {} updated successfully", id);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete a task",
            description = "Deletes a task by its unique ID.",
            tags = {"Tasks"}
    )
    @Parameter(
            name = "id",
            description = "Unique identifier of the task to delete",
            required = true,
            example = "123e4567-e89b-12d3-a456-426614174004"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable @TaskExists UUID id) {
        taskService.deleteTask(id);
        log.info("Task with ID: {} deleted successfully", id);

        return ResponseEntity.noContent()
                .build();
    }
}
