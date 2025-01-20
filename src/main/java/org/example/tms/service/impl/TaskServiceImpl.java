package org.example.tms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.tms.annotation.ExecutionTime;
import org.example.tms.dto.requests.create.CreateTaskRequestDto;
import org.example.tms.dto.requests.update.UpdateTaskRequestDto;
import org.example.tms.dto.responses.TaskResponseDto;
import org.example.tms.exception.custom.EntitySaveException;
import org.example.tms.exception.custom.TaskNotFoundException;
import org.example.tms.mapper.TaskMapper;
import org.example.tms.model.Comment;
import org.example.tms.model.Task;
import org.example.tms.model.TaskAssignee;
import org.example.tms.model.User;
import org.example.tms.model.enums.UserRole;
import org.example.tms.repository.TaskRepository;
import org.example.tms.service.TaskService;
import org.example.tms.service.UserService;
import org.example.tms.validator.PermissionValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation for managing tasks.
 * Provides functionality to create, update, delete and retrieve tasks with support for paginated results
 * and permission validation.
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final UserService userService;
    private final TaskRepository taskRepository;
    private final PermissionValidator permissionValidator;

    /**
     * Retrieves a task entity by its ID.
     *
     * @param id the unique identifier of the task
     * @return the {@link Task} entity
     * @throws TaskNotFoundException if the task is not found
     */
    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public Task getTaskEntityById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));
    }

    /**
     * Retrieves task details as a DTO by its ID.
     *
     * @param id the unique identifier of the task
     * @return a {@link TaskResponseDto} containing task details
     * @throws TaskNotFoundException if the task is not found
     */
    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(UUID id) {
        return taskRepository.findById(id)
                .map(taskMapper::toTaskResponseDto)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));
    }

    /**
     * Retrieves paginated tasks authored by a specific user.
     *
     * @param authorId the unique identifier of the author
     * @param pageable pagination information
     * @return a {@link Page} of {@link TaskResponseDto}
     */
    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public Page<TaskResponseDto> getTasksByAuthor(UUID authorId, Pageable pageable) {
        Page<Task> tasks = taskRepository.findAllByAuthor_Id(authorId, pageable);
        tasks.forEach(this::loadTaskAssociations);

        return tasks.map(taskMapper::toTaskResponseDto);
    }

    /**
     * Retrieves paginated tasks assigned to a specific user.
     *
     * @param assigneeId the unique identifier of the assignee
     * @param pageable   pagination information
     * @return a {@link Page} of {@link TaskResponseDto}
     */
    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public Page<TaskResponseDto> getTasksByAssignee(UUID assigneeId, Pageable pageable) {
        Page<Task> tasks = taskRepository.findAllByTaskAssignees_Assignee_Id(assigneeId, pageable);
        tasks.forEach(this::loadTaskAssociations);

        return tasks.map(taskMapper::toTaskResponseDto);

    }

    /**
     * Retrieves all tasks with pagination support.
     *
     * @param pageable pagination information
     * @return a {@link Page} of {@link TaskResponseDto}
     */
    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public Page<TaskResponseDto> getAllTasks(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAllWithDetails(pageable);
        tasks.forEach(this::loadTaskAssociations);

        return tasks.map(taskMapper::toTaskResponseDto);
    }

    /**
     * Creates a new task based on the provided request data.
     *
     * @param request the {@link CreateTaskRequestDto} containing task details
     * @return a {@link TaskResponseDto} with the created task's details
     * @throws EntitySaveException if the task could not be saved
     */
    @Override
    @Transactional
    public TaskResponseDto createTask(CreateTaskRequestDto request) {
        return Optional.of(request)
                .map(taskMapper::toTaskForCreate)
                .map(taskRepository::save)
                .map(taskMapper::toTaskResponseDto)
                .orElseThrow(() -> new EntitySaveException("Failed to save task."));
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the unique identifier of the task
     * @throws TaskNotFoundException if the task is not found
     */
    @Override
    @Transactional
    public void deleteTask(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));

        taskRepository.delete(task);
    }

    /**
     * Saves a task entity directly to the database.
     *
     * @param task the {@link Task} entity to save
     */
    @Override
    @Transactional
    public void save(Task task) {
        taskRepository.save(task);
    }

    /**
     * Updates an existing task with new details based on user permissions.
     *
     * @param id          the unique identifier of the task
     * @param request     the {@link UpdateTaskRequestDto} containing updated task details
     * @param currentUser the user attempting the update
     * @return a {@link TaskResponseDto} with updated task details
     * @throws TaskNotFoundException if the task is not found
     * @throws EntitySaveException   if the task could not be saved
     */
    @Override
    @Transactional
    public TaskResponseDto updateTask(UUID id, UpdateTaskRequestDto request, User currentUser) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));

        permissionValidator.validateAssigneePermission(currentUser, existingTask);

        if (currentUser.getRole() == UserRole.ROLE_ADMIN) {
            setTaskEntityForAdmin(existingTask, request);

            return returnTaskResponseDtoValue(existingTask);
        }

        setTaskEntityForUser(existingTask, request);

        return returnTaskResponseDtoValue(existingTask);
    }

    private TaskResponseDto returnTaskResponseDtoValue(Task task) {
        return Optional.of(task)
                .map(taskRepository::save)
                .map(taskMapper::toTaskResponseDto)
                .orElseThrow(() -> new EntitySaveException("Failed to save task."));
    }

    /**
     * Loads task associations such as assignees and comments for a task.
     *
     * @param task the {@link Task} entity whose associations should be loaded
     */
    private void loadTaskAssociations(Task task) {
        List<TaskAssignee> taskAssignees = taskRepository.fetchTaskWithTaskAssignees(task.getId());
        task.setTaskAssignees(taskAssignees);

        List<Comment> comments = taskRepository.fetchTaskWithComments(task.getId());
        task.setComments(comments);
    }

    private void setTaskEntityForAdmin(Task task, UpdateTaskRequestDto request) {
        setGeneralTaskEntity(task, request);
        task.setAuthor(userService.getUserEntityById(request.getAuthorId()));
        task.setCreatedAt(request.getCreatedAt() != null ? request.getCreatedAt() : LocalDateTime.now());
        task.setUpdatedAt(request.getUpdatedAt() != null ? request.getUpdatedAt() : LocalDateTime.now());
    }

    private void setTaskEntityForUser(Task task, UpdateTaskRequestDto request) {
        setGeneralTaskEntity(task, request);
        task.setAuthor(task.getAuthor());
        task.setCreatedAt(task.getCreatedAt());
        task.setUpdatedAt(task.getUpdatedAt());
    }

    private void setGeneralTaskEntity(Task task, UpdateTaskRequestDto request) {
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
    }
}
