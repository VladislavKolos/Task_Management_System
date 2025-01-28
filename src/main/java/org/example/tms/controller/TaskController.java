package org.example.tms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.dto.requests.create.CreateTaskRequestDto;
import org.example.tms.dto.requests.update.UpdateTaskRequestDto;
import org.example.tms.dto.responses.TaskResponseDto;
import org.example.tms.model.User;
import org.example.tms.service.TaskService;
import org.example.tms.service.UriService;
import org.example.tms.util.CurrentUserUtil;
import org.example.tms.validator.constraint.annotation.TaskExists;
import org.example.tms.validator.constraint.annotation.UserExists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
    private final UriService uriService;
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
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDto getTaskById(@PathVariable @TaskExists UUID id) {
        TaskResponseDto response = taskService.getTaskById(id);
        log.info("Retrieved task with ID: {}", id);

        return response;
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
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskResponseDto> getTasksByAuthor(
            @PathVariable @UserExists UUID authorId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TaskResponseDto> response = taskService.getTasksByAuthor(authorId, pageable);
        log.info("Retrieved tasks for Author with ID: {}", authorId);

        return response;
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
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskResponseDto> getTasksByAssignee(
            @PathVariable @UserExists UUID assigneeId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TaskResponseDto> response = taskService.getTasksByAssignee(assigneeId, pageable);
        log.info("Retrieved tasks for assignee with ID: {}", assigneeId);

        return response;
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
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskResponseDto> getAllTasks(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TaskResponseDto> response = taskService.getAllTasks(pageable);
        log.info("Retrieved all tasks with page number: {}, page size: {}, sort: {}",
                pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort());

        return response;
    }

    @Operation(
            summary = "Create a new task",
            description = "Creates a new task with the provided details.",
            tags = {"Tasks"}
    )
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody CreateTaskRequestDto request) {
        TaskResponseDto response = taskService.createTask(request);
        log.info("Task created successfully with ID: {}", response.id());

        String resourceUri = uriService.createTaskUri(response.id());

        return ResponseEntity.created(URI.create(resourceUri))
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
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDto updateTask(
            @PathVariable @TaskExists UUID id,
            @Valid @RequestBody UpdateTaskRequestDto request) {
        User currentUser = CurrentUserUtil.getCurrentUser();

        TaskResponseDto response = taskService.updateTask(id, request, currentUser);
        log.info("Task with ID: {} updated successfully", id);

        return response;
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable @TaskExists UUID id) {
        taskService.deleteTask(id);
        log.info("Task with ID: {} deleted successfully", id);
    }
}
