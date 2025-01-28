package org.example.tms.service;

import org.example.tms.dto.requests.create.CreateTaskRequestDto;
import org.example.tms.dto.requests.update.UpdateTaskRequestDto;
import org.example.tms.dto.responses.TaskResponseDto;
import org.example.tms.model.Task;
import org.example.tms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TaskService {
    Task getTaskEntityById(UUID id);

    TaskResponseDto getTaskById(UUID id);

    Page<TaskResponseDto> getTasksByAuthor(UUID authorId, Pageable pageable);

    Page<TaskResponseDto> getTasksByAssignee(UUID assigneeId, Pageable pageable);

    Page<TaskResponseDto> getAllTasks(Pageable pageable);

    TaskResponseDto createTask(CreateTaskRequestDto request);

    void deleteTask(UUID id);

    void save(Task task);

    TaskResponseDto updateTask(UUID id, UpdateTaskRequestDto request, User currentUser);
}
