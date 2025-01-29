package org.example.tms.service.impl;

import org.example.tms.dto.requests.create.CreateTaskAssigneeRequestDto;
import org.example.tms.dto.responses.TaskAssigneeResponseDto;
import org.example.tms.dto.responses.TaskResponseDto;
import org.example.tms.dto.responses.UserResponseDto;
import org.example.tms.exception.TaskNotFoundException;
import org.example.tms.exception.UserNotFoundException;
import org.example.tms.mapper.TaskAssigneeMapper;
import org.example.tms.model.Task;
import org.example.tms.model.TaskAssignee;
import org.example.tms.model.User;
import org.example.tms.model.enums.TaskPriority;
import org.example.tms.model.enums.TaskStatus;
import org.example.tms.model.enums.UserRole;
import org.example.tms.repository.TaskAssigneeRepository;
import org.example.tms.service.TaskService;
import org.example.tms.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskAssigneeServiceImplTest {
    private static final UUID ASSIGNEE_ID = UUID.randomUUID();
    private static final UUID TASK_ID = UUID.randomUUID();

    @InjectMocks
    private TaskAssigneeServiceImpl taskAssigneeService;

    @Mock
    private UserService userService;

    @Mock
    private TaskService taskService;

    @Mock
    private TaskAssigneeMapper taskAssigneeMapper;

    @Mock
    private TaskAssigneeRepository taskAssigneeRepository;

    private Task task;
    private User user;
    private CreateTaskAssigneeRequestDto createTaskAssigneeRequestDto;
    private TaskAssigneeResponseDto taskAssigneeResponseDto;

    @BeforeEach
    public void setUp() {
        task = Task.builder()
                .id(TASK_ID)
                .title("Task Title")
                .description("Task Description")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.HIGH)
                .build();

        user = User.builder()
                .id(ASSIGNEE_ID)
                .email("user@example.com")
                .role(UserRole.ROLE_USER)
                .build();

        taskAssigneeResponseDto = TaskAssigneeResponseDto.builder()
                .id(UUID.randomUUID())
                .task(TaskResponseDto.builder()
                        .id(TASK_ID)
                        .title("Task Title")
                        .description("Task Description")
                        .status(TaskStatus.PENDING)
                        .priority(TaskPriority.HIGH)
                        .build())
                .assignee(UserResponseDto.builder()
                        .id(ASSIGNEE_ID)
                        .email("user@example.com")
                        .role(UserRole.ROLE_USER)
                        .build())
                .assignedAt(LocalDateTime.now())
                .build();

        createTaskAssigneeRequestDto = CreateTaskAssigneeRequestDto.builder()
                .taskId(TASK_ID)
                .assigneeId(ASSIGNEE_ID)
                .assignedAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void testAssignTaskToUser_Success() {
        TaskAssignee taskAssignee = TaskAssignee.builder()
                .task(task)
                .assignee(user)
                .assignedAt(LocalDateTime.now())
                .build();

        when(taskService.getTaskEntityById(TASK_ID)).thenReturn(task);
        when(userService.getUserEntityById(ASSIGNEE_ID)).thenReturn(user);
        when(taskAssigneeRepository.save(eq(taskAssignee))).thenReturn(taskAssignee);
        when(taskAssigneeMapper.toTaskAssigneeResponseDto(taskAssignee)).thenReturn(taskAssigneeResponseDto);

        TaskAssigneeResponseDto result = taskAssigneeService.assignTaskToUser(createTaskAssigneeRequestDto);

        assertEquals(taskAssigneeResponseDto.id(), result.id());
        verify(taskService).getTaskEntityById(TASK_ID);
        verify(userService).getUserEntityById(ASSIGNEE_ID);
    }

    @Test
    public void testAssignTaskToUser_TaskNotFound() {
        when(taskService.getTaskEntityById(TASK_ID)).thenThrow(TaskNotFoundException.class);

        assertThrows(TaskNotFoundException.class,
                () -> taskAssigneeService.assignTaskToUser(createTaskAssigneeRequestDto));

        verify(taskService).getTaskEntityById(TASK_ID);
    }

    @Test
    public void testAssignTaskToUser_UserNotFound() {
        when(taskService.getTaskEntityById(TASK_ID)).thenReturn(task);
        when(userService.getUserEntityById(ASSIGNEE_ID)).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class,
                () -> taskAssigneeService.assignTaskToUser(createTaskAssigneeRequestDto));

        verify(taskService).getTaskEntityById(TASK_ID);
        verify(userService).getUserEntityById(ASSIGNEE_ID);
    }
}