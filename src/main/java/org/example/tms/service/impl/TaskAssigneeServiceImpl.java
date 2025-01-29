package org.example.tms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.tms.dto.requests.create.CreateTaskAssigneeRequestDto;
import org.example.tms.dto.responses.TaskAssigneeResponseDto;
import org.example.tms.exception.TaskAssigneeNotFoundException;
import org.example.tms.mapper.TaskAssigneeMapper;
import org.example.tms.model.TaskAssignee;
import org.example.tms.repository.TaskAssigneeRepository;
import org.example.tms.service.TaskAssigneeService;
import org.example.tms.service.TaskService;
import org.example.tms.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service implementation for managing task assignments to Users.
 * Provides functionality to assign tasks to Users and retrieve assignment details.
 */
@Service
@RequiredArgsConstructor
public class TaskAssigneeServiceImpl implements TaskAssigneeService {
    private final UserService userService;
    private final TaskService taskService;
    private final TaskAssigneeMapper taskAssigneeMapper;
    private final TaskAssigneeRepository taskAssigneeRepository;

    /**
     * Assigns a task to a User based on the provided request data.
     *
     * @param request the request data containing task and assignee details
     * @return a {@link TaskAssigneeResponseDto} with the details of the task assignment
     * @throws TaskAssigneeNotFoundException if the User or task with the specified IDs is not found
     */
    @Override
    @Transactional
    public TaskAssigneeResponseDto assignTaskToUser(CreateTaskAssigneeRequestDto request) {
        TaskAssignee taskAssignee = buildTaskAssigneeEntity(request);

        return Optional.of(taskAssignee)
                .map(taskAssigneeRepository::save)
                .map(taskAssigneeMapper::toTaskAssigneeResponseDto)
                .orElseThrow(() -> new TaskAssigneeNotFoundException(request.getAssigneeId(), request.getTaskId()));
    }

    /**
     * Builds a {@link TaskAssignee} entity from the provided request data.
     *
     * @param request the request data containing task and assignee details
     * @return the constructed {@link TaskAssignee} entity
     */
    private TaskAssignee buildTaskAssigneeEntity(CreateTaskAssigneeRequestDto request) {
        return TaskAssignee.builder()
                .task(taskService.getTaskEntityById(request.getTaskId()))
                .assignee(userService.getUserEntityById(request.getAssigneeId()))
                .assignedAt(request.getAssignedAt() != null ? request.getAssignedAt() : LocalDateTime.now())
                .build();
    }
}
