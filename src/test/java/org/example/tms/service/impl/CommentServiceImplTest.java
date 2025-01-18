package org.example.tms.service.impl;

import org.example.tms.dto.requests.create.CreateCommentRequestDto;
import org.example.tms.dto.responses.CommentResponseDto;
import org.example.tms.dto.responses.TaskResponseDto;
import org.example.tms.dto.responses.UserResponseDto;
import org.example.tms.exception.custom.CommentNotFoundException;
import org.example.tms.exception.custom.EntitySaveException;
import org.example.tms.exception.custom.PermissionDeniedException;
import org.example.tms.mapper.CommentMapper;
import org.example.tms.model.Comment;
import org.example.tms.model.Task;
import org.example.tms.model.User;
import org.example.tms.model.enums.TaskPriority;
import org.example.tms.model.enums.TaskStatus;
import org.example.tms.model.enums.UserRole;
import org.example.tms.repository.CommentRepository;
import org.example.tms.service.TaskService;
import org.example.tms.service.UserService;
import org.example.tms.validator.PermissionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    private static final UUID TASK_ID = UUID.randomUUID();
    private static final UUID COMMENT_ID = UUID.randomUUID();
    private static final UUID AUTHOR_ID = UUID.randomUUID();
    private static final UUID CURRENT_USER_ID = UUID.randomUUID();
    @InjectMocks
    private CommentServiceImpl commentService;
    @Mock
    private UserService userService;
    @Mock
    private TaskService taskService;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PermissionValidator permissionValidator;
    private Comment comment;
    private CommentResponseDto commentResponseDto;
    private CreateCommentRequestDto createCommentRequestDto;
    private User currentUser;
    private Task task;

    @BeforeEach
    public void setUp() {
        task = Task.builder()
                .id(TASK_ID)
                .title("Test Task")
                .description("Task description")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.HIGH)
                .author(User.builder()
                        .id(AUTHOR_ID)
                        .email("author@example.com")
                        .role(UserRole.ROLE_USER)
                        .password("password")
                        .build())
                .build();

        comment = Comment.builder()
                .id(COMMENT_ID)
                .content("Test Comment")
                .task(task)
                .author(task.getAuthor())
                .build();

        commentResponseDto = CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .task(TaskResponseDto.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .description(task.getDescription())
                        .build())
                .author(UserResponseDto.builder()
                        .id(task.getAuthor()
                                .getId())
                        .email(task.getAuthor()
                                .getEmail())
                        .build())
                .build();

        createCommentRequestDto = CreateCommentRequestDto.builder()
                .content("Test Comment")
                .taskId(TASK_ID)
                .build();

        currentUser = User.builder()
                .id(CURRENT_USER_ID)
                .role(UserRole.ROLE_USER)
                .email("currentuser@example.com")
                .password("password")
                .build();
    }

    @Test
    public void testGetCommentById_Success() {
        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));
        when(commentMapper.toCommentResponseDto(comment)).thenReturn(commentResponseDto);

        CommentResponseDto result = commentService.getCommentById(COMMENT_ID);

        assertEquals(commentResponseDto, result);
        verify(commentRepository).findById(COMMENT_ID);
        verify(commentMapper).toCommentResponseDto(comment);
    }

    @Test
    public void testGetCommentById_NotFound() {
        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.getCommentById(COMMENT_ID));
        verify(commentRepository).findById(COMMENT_ID);
    }

    @Test
    public void testGetCommentsByTaskId_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> commentPage = new PageImpl<>(List.of(comment));

        when(commentRepository.findAllByTask_Id(TASK_ID, pageable)).thenReturn(commentPage);
        when(commentMapper.toCommentResponseDto(comment)).thenReturn(commentResponseDto);

        Page<CommentResponseDto> result = commentService.getCommentsByTaskId(TASK_ID, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(commentResponseDto, result.getContent()
                .get(0));
        verify(commentRepository).findAllByTask_Id(TASK_ID, pageable);
        verify(commentMapper).toCommentResponseDto(comment);
    }

    @Test
    public void testAddComment_Success() {
        try (MockedStatic<SecurityContextHolder> ignored = mockStatic(SecurityContextHolder.class)) {
            mockSecurityContext();

            when(taskService.getTaskEntityById(TASK_ID)).thenReturn(task);
            when(userService.getUserEntityById(CURRENT_USER_ID)).thenReturn(currentUser);
            doNothing().when(permissionValidator)
                    .validateAssigneePermission(currentUser, task);

            Comment commentToSave = Comment.builder()
                    .content("Test Comment")
                    .author(currentUser)
                    .task(task)
                    .build();

            when(commentMapper.toCommentResponseDto(commentToSave)).thenReturn(commentResponseDto);
            when(commentRepository.save(eq(commentToSave))).thenReturn(commentToSave);

            CommentResponseDto result = commentService.addComment(createCommentRequestDto);

            assertEquals(commentResponseDto, result);

            ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
            verify(commentRepository).save(commentCaptor.capture());

            assertEquals(commentToSave, commentCaptor.getValue());

            ArgumentCaptor<Comment> commentMapperCaptor = ArgumentCaptor.forClass(Comment.class);
            verify(commentMapper).toCommentResponseDto(commentMapperCaptor.capture());

            assertEquals(commentToSave, commentMapperCaptor.getValue());
        }
    }

    @Test
    public void testAddComment_FailedSave() {
        try (MockedStatic<SecurityContextHolder> ignored = mockStatic(SecurityContextHolder.class)) {
            mockSecurityContext();

            when(taskService.getTaskEntityById(TASK_ID)).thenReturn(task);
            when(userService.getUserEntityById(CURRENT_USER_ID)).thenReturn(currentUser);
            doNothing().when(permissionValidator)
                    .validateAssigneePermission(currentUser, task);

            Comment commentToSave = Comment.builder()
                    .content("Test Comment")
                    .author(currentUser)
                    .task(task)
                    .build();

            when(commentRepository.save(eq(commentToSave))).thenReturn(commentToSave);

            ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);

            assertThrows(EntitySaveException.class, () -> commentService.addComment(createCommentRequestDto));
            verify(commentRepository).save(commentCaptor.capture());
        }
    }

    @Test
    public void testDeleteComment_Success() {
        try (MockedStatic<SecurityContextHolder> ignored = mockStatic(SecurityContextHolder.class)) {
            mockSecurityContext();

            when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

            doNothing().when(permissionValidator)
                    .validateCommentOwnershipOrAdmin(currentUser, comment);

            commentService.deleteComment(COMMENT_ID);

            verify(commentRepository).findById(COMMENT_ID);
            verify(commentRepository).delete(comment);
        }
    }

    @Test
    public void testDeleteComment_NotFound() {
        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment(COMMENT_ID));
        verify(commentRepository).findById(COMMENT_ID);
    }

    @Test
    public void testDeleteComment_PermissionDenied() {
        try (MockedStatic<SecurityContextHolder> ignored = mockStatic(SecurityContextHolder.class)) {
            mockSecurityContext();

            when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

            doThrow(new PermissionDeniedException("Permission Denied"))
                    .when(permissionValidator)
                    .validateCommentOwnershipOrAdmin(currentUser, comment);

            assertThrows(PermissionDeniedException.class, () -> commentService.deleteComment(COMMENT_ID));

            verify(commentRepository).findById(COMMENT_ID);
            verify(permissionValidator).validateCommentOwnershipOrAdmin(currentUser, comment);
        }
    }

    private void mockSecurityContext() {
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(currentUser);
    }
}