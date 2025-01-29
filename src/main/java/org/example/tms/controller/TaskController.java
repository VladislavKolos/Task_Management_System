package org.example.tms.controller;

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

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final UriService uriService;
    private final TaskService taskService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDto getTaskById(@PathVariable @TaskExists UUID id) {
        log.info("Incoming request to retrieve task with ID: {}", id);

        TaskResponseDto response = taskService.getTaskById(id);
        log.info("Retrieved task with ID: {}", id);

        return response;
    }

    @GetMapping("/author/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskResponseDto> getTasksByAuthor(
            @PathVariable @UserExists UUID authorId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Incoming request to retrieve tasks for author with ID: {}. Pageable: {}", authorId, pageable);

        Page<TaskResponseDto> response = taskService.getTasksByAuthor(authorId, pageable);
        log.info("Retrieved tasks for Author with ID: {}", authorId);

        return response;
    }

    @GetMapping("/assignee/{assigneeId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskResponseDto> getTasksByAssignee(
            @PathVariable @UserExists UUID assigneeId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Incoming request to retrieve tasks for assignee with ID: {}. Pageable: {}", assigneeId, pageable);

        Page<TaskResponseDto> response = taskService.getTasksByAssignee(assigneeId, pageable);
        log.info("Retrieved tasks for assignee with ID: {}", assigneeId);

        return response;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TaskResponseDto> getAllTasks(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Incoming request to retrieve all tasks. Pageable: {}", pageable);

        Page<TaskResponseDto> response = taskService.getAllTasks(pageable);
        log.info("Retrieved all tasks with page number: {}, page size: {}, sort: {}",
                pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort());

        return response;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody CreateTaskRequestDto request) {
        log.info("Incoming request to create task with details: {}", request);

        TaskResponseDto response = taskService.createTask(request);
        log.info("Task created successfully with ID: {}", response.id());

        String resourceUri = uriService.createTaskUri(response.id());
        log.info("Task resource URI created: {}", resourceUri);

        return ResponseEntity.created(URI.create(resourceUri))
                .body(response);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDto updateTask(
            @PathVariable @TaskExists UUID id,
            @Valid @RequestBody UpdateTaskRequestDto request) {
        log.info("Incoming request to update task with ID: {}. Update details: {}", id, request);

        User currentUser = CurrentUserUtil.getCurrentUser();

        TaskResponseDto response = taskService.updateTask(id, request, currentUser);
        log.info("Task with ID: {} updated successfully", id);

        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable @TaskExists UUID id) {
        log.info("Incoming request to delete task with ID: {}", id);

        taskService.deleteTask(id);
        log.info("Task with ID: {} deleted successfully", id);
    }
}
