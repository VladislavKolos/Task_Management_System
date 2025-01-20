package org.example.tms.service.impl;

import org.example.tms.dto.requests.create.CreateTaskRequestDto;
import org.example.tms.dto.requests.update.UpdateTaskRequestDto;
import org.example.tms.dto.responses.TaskResponseDto;
import org.example.tms.exception.custom.TaskNotFoundException;
import org.example.tms.mapper.TaskMapper;
import org.example.tms.model.Task;
import org.example.tms.model.User;
import org.example.tms.model.enums.TaskPriority;
import org.example.tms.model.enums.TaskStatus;
import org.example.tms.model.enums.UserRole;
import org.example.tms.repository.TaskRepository;
import org.example.tms.service.UserService;
import org.example.tms.validator.PermissionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
    private static final UUID AUTHOR_ID = UUID.randomUUID();
    private static final UUID TASK_ID = UUID.randomUUID();

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserService userService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private PermissionValidator permissionValidator;

    private Task task;
    private TaskResponseDto taskResponseDto;
    private CreateTaskRequestDto createTaskRequestDto;
    private UpdateTaskRequestDto updateTaskRequestDto;
    private User currentUser;

    @BeforeEach
    public void setUp() {
        task = Task.builder()
                .id(TASK_ID)
                .title("Test Task")
                .description("Description")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.HIGH)
                .author(User.builder()
                        .id(AUTHOR_ID)
                        .build())
                .build();

        taskResponseDto = TaskResponseDto.builder()
                .id(task.getId())
                .title("Test Task")
                .description("Description")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.HIGH)
                .build();

        createTaskRequestDto = CreateTaskRequestDto.builder()
                .title("Test Task")
                .description("Description")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.HIGH)
                .build();

        updateTaskRequestDto = UpdateTaskRequestDto.builder()
                .title("Updated Task")
                .description("Updated Description")
                .status(TaskStatus.COMPLETED)
                .priority(TaskPriority.LOW)
                .authorId(AUTHOR_ID)
                .build();

        currentUser = User.builder()
                .id(UUID.randomUUID())
                .role(UserRole.ROLE_USER)
                .email("test@test.com")
                .password("test")
                .build();
    }

    @Test
    public void testGetTaskEntityById_Success() {
        UUID taskId = task.getId();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskEntityById(taskId);

        assertEquals(task, result);
        verify(taskRepository).findById(taskId);
    }

    @Test
    public void testGetTaskEntityById_NotFound() {
        UUID taskId = UUID.randomUUID();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskEntityById(taskId));
        verify(taskRepository).findById(taskId);
    }

    @Test
    public void testGetTaskById_Success() {
        UUID taskId = task.getId();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toTaskResponseDto(task)).thenReturn(taskResponseDto);

        TaskResponseDto result = taskService.getTaskById(taskId);

        assertEquals(taskResponseDto, result);
        verify(taskRepository).findById(taskId);
        verify(taskMapper).toTaskResponseDto(task);
    }

    @Test
    public void testCreateTask_Success() {
        when(taskMapper.toTaskForCreate(createTaskRequestDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toTaskResponseDto(task)).thenReturn(taskResponseDto);

        TaskResponseDto result = taskService.createTask(createTaskRequestDto);

        assertEquals(taskResponseDto, result);
        verify(taskMapper).toTaskForCreate(createTaskRequestDto);
        verify(taskRepository).save(task);
        verify(taskMapper).toTaskResponseDto(task);
    }

    @Test
    public void testDeleteTask_Success() {
        UUID taskId = task.getId();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.deleteTask(taskId);

        verify(taskRepository).findById(taskId);
        verify(taskRepository).delete(task);
    }

    @Test
    public void testUpdateTask_ForAdminSuccess() {
        UUID taskId = task.getId();
        UUID authorId = task.getAuthor()
                .getId();
        currentUser.setRole(UserRole.ROLE_ADMIN);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userService.getUserEntityById(authorId)).thenReturn(task.getAuthor());
        doNothing().when(permissionValidator)
                .validateAssigneePermission(currentUser, task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toTaskResponseDto(task)).thenReturn(taskResponseDto);

        TaskResponseDto result = taskService.updateTask(taskId, updateTaskRequestDto, currentUser);

        assertEquals(taskResponseDto, result);
        verify(permissionValidator).validateAssigneePermission(currentUser, task);
        verify(taskRepository).save(task);
        verify(taskMapper).toTaskResponseDto(task);
    }

    @Test
    public void testGetTasksByAuthor_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        UUID authorId = AUTHOR_ID;

        Page<Task> taskPage = new PageImpl<>(List.of(task));

        when(taskRepository.findAllByAuthor_Id(authorId, pageable)).thenReturn(taskPage);
        when(taskMapper.toTaskResponseDto(task)).thenReturn(taskResponseDto);

        Page<TaskResponseDto> result = taskService.getTasksByAuthor(authorId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(taskResponseDto, result.getContent()
                .get(0));
        verify(taskRepository).findAllByAuthor_Id(authorId, pageable);
    }
}