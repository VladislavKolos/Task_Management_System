package org.example.tms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.tms.aspect.logging.annotation.ExecutionTime;
import org.example.tms.dto.requests.create.CreateCommentRequestDto;
import org.example.tms.dto.responses.CommentResponseDto;
import org.example.tms.exception.CommentNotFoundException;
import org.example.tms.exception.EntitySaveException;
import org.example.tms.mapper.CommentMapper;
import org.example.tms.model.Comment;
import org.example.tms.model.Task;
import org.example.tms.model.User;
import org.example.tms.repository.CommentRepository;
import org.example.tms.service.CommentService;
import org.example.tms.service.TaskService;
import org.example.tms.service.UserService;
import org.example.tms.util.CurrentUserUtil;
import org.example.tms.validator.PermissionValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation for managing comments.
 * Provides methods for CRUD operations on comments and validation logic.
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserService userService;
    private final TaskService taskService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final PermissionValidator permissionValidator;

    /**
     * Retrieves a comment by its ID.
     *
     * @param id the UUID of the comment
     * @return the corresponding {@link CommentResponseDto}
     * @throws CommentNotFoundException if no comment is found with the given ID
     */
    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public CommentResponseDto getCommentById(UUID id) {
        return commentRepository.findById(id)
                .map(commentMapper::toCommentResponseDto)
                .orElseThrow(() -> new CommentNotFoundException(id));
    }

    /**
     * Retrieves a paginated list of comments for a specific task.
     *
     * @param taskId   the UUID of the task
     * @param pageable pagination information
     * @return a paginated list of {@link CommentResponseDto}
     */
    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getCommentsByTaskId(UUID taskId, Pageable pageable) {
        return commentRepository.findAllByTask_Id(taskId, pageable)
                .map(commentMapper::toCommentResponseDto);
    }

    /**
     * Adds a new comment to a task.
     *
     * @param request the {@link CreateCommentRequestDto} containing comment details
     * @return the saved {@link CommentResponseDto}
     * @throws EntitySaveException if saving the comment fails
     */
    @Override
    @Transactional
    public CommentResponseDto addComment(CreateCommentRequestDto request) {
        Task task = taskService.getTaskEntityById(request.getTaskId());
        User author = userService.getUserEntityById(CurrentUserUtil.getCurrentUser()
                .getId());

        permissionValidator.validateAssigneePermission(author, task);

        Comment comment = buildCommentEntity(request, CurrentUserUtil.getCurrentUser());

        return Optional.of(comment)
                .map(commentRepository::save)
                .map(commentMapper::toCommentResponseDto)
                .orElseThrow(() -> new EntitySaveException(EntitySaveException.ErrorType.COMMENT_SAVE_ERROR));
    }

    /**
     * Deletes a comment by its ID.
     *
     * @param id the UUID of the comment to delete
     * @throws CommentNotFoundException if no comment is found with the given ID
     */
    @Override
    @Transactional
    public void deleteComment(UUID id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
        User author = CurrentUserUtil.getCurrentUser();

        permissionValidator.validateCommentOwnershipOrAdmin(author, comment);

        commentRepository.delete(comment);
    }

    /**
     * Builds a {@link Comment} entity from the request DTO and the current User.
     *
     * @param request     the {@link CreateCommentRequestDto} containing comment details
     * @param currentUser the current logged-in {@link User}
     * @return the constructed {@link Comment} entity
     */
    private Comment buildCommentEntity(CreateCommentRequestDto request, User currentUser) {
        return Comment.builder()
                .content(request.getContent())
                .author(userService.getUserEntityById(currentUser.getId()))
                .task(taskService.getTaskEntityById(request.getTaskId()))
                .build();
    }
}
