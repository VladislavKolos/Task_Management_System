package org.example.tms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.dto.requests.create.CreateTaskAssigneeRequestDto;
import org.example.tms.dto.responses.TaskAssigneeResponseDto;
import org.example.tms.service.TaskAssigneeService;
import org.example.tms.service.UriService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/tasks-assignees")
@RequiredArgsConstructor
public class TaskAssigneeController {
    private final UriService uriService;
    private final TaskAssigneeService taskAssigneeService;

    @PostMapping
    public ResponseEntity<TaskAssigneeResponseDto> assignTaskToUser(
            @Valid @RequestBody CreateTaskAssigneeRequestDto request) {
        log.info("Incoming request to assign task with ID: {} to user with ID: {}",
                request.getTaskId(), request.getAssigneeId());


        TaskAssigneeResponseDto response = taskAssigneeService.assignTaskToUser(request);
        log.info("Task with ID: {} successfully assigned to User with ID: {}", request.getTaskId(),
                request.getAssigneeId());

        String resourceUri = uriService.createTaskAssigneeUri(response.id());
        log.info("Task Assignee resource URI created: {}", resourceUri);

        return ResponseEntity.created(URI.create(resourceUri))
                .body(response);
    }
}
