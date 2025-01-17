package org.example.tms.controller;

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

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable @TaskExists UUID id) {
        TaskResponseDto response = taskService.getTaskById(id);
        log.info("Retrieved task with ID: {}", id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<Page<TaskResponseDto>> getTasksByAuthor(
            @PathVariable @UserExists UUID authorId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TaskResponseDto> tasks = taskService.getTasksByAuthor(authorId, pageable);
        log.info("Retrieved tasks for Author with ID: {}", authorId);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<Page<TaskResponseDto>> getTasksByAssignee(
            @PathVariable @UserExists UUID assigneeId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TaskResponseDto> tasks = taskService.getTasksByAssignee(assigneeId, pageable);
        log.info("Retrieved tasks for assignee with ID: {}", assigneeId);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponseDto>> getAllTasks(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TaskResponseDto> tasks = taskService.getAllTasks(pageable);
        log.info("Retrieved all tasks");

        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody CreateTaskRequestDto request) {
        TaskResponseDto response = taskService.createTask(request);
        log.info("Task created successfully with ID: {}", response.getId());

        return ResponseEntity.created(URI.create("/api/tasks/" + response.getId()))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable @TaskExists UUID id,
            @Valid @RequestBody UpdateTaskRequestDto request) {
        User currentUser = CurrentUserUtil.getCurrentUser();

        TaskResponseDto response = taskService.updateTask(id, request, currentUser);
        log.info("Task with ID: {} updated successfully", id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable @TaskExists UUID id) {
        taskService.deleteTask(id);
        log.info("Task with ID: {} deleted successfully", id);

        return ResponseEntity.noContent()
                .build();
    }
}
