package org.example.tms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.dto.requests.create.CreateTaskAssigneeRequestDto;
import org.example.tms.dto.responses.TaskAssigneeResponseDto;
import org.example.tms.service.TaskAssigneeService;
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
    private final TaskAssigneeService taskAssigneeService;

    @PostMapping
    public ResponseEntity<TaskAssigneeResponseDto> assignTaskToUser(
            @Valid @RequestBody CreateTaskAssigneeRequestDto request) {

        TaskAssigneeResponseDto response = taskAssigneeService.assignTaskToUser(request);
        log.info("Task with ID: {} successfully assigned to User with ID: {}", request.getTaskId(),
                request.getAssigneeId());

        return ResponseEntity.created(URI.create("/api/tasks-assignees/" + response.getId()))
                .body(response);
    }
}
