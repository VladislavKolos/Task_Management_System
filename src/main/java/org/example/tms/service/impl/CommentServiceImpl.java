package org.example.tms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.tms.annotation.ExecutionTime;
import org.example.tms.dto.requests.create.CreateCommentRequestDto;
import org.example.tms.dto.responses.CommentResponseDto;
import org.example.tms.exception.custom.CommentNotFoundException;
import org.example.tms.exception.custom.EntitySaveException;
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

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserService userService;
    private final TaskService taskService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final PermissionValidator permissionValidator;


    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public CommentResponseDto getCommentById(UUID id) {
        return commentRepository.findById(id)
                .map(commentMapper::toCommentResponseDto)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + id));
    }

    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public Page<CommentResponseDto> getCommentsByTaskId(UUID taskId, Pageable pageable) {
        return commentRepository.findAllByTask_Id(taskId, pageable)
                .map(commentMapper::toCommentResponseDto);
    }

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
                .orElseThrow(() -> new EntitySaveException("Failed to save comment."));
    }

    @Override
    @Transactional
    public void deleteComment(UUID id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + id));
        User author = CurrentUserUtil.getCurrentUser();

        permissionValidator.validateCommentOwnershipOrAdmin(author, comment);

        commentRepository.delete(comment);
    }

    private Comment buildCommentEntity(CreateCommentRequestDto request, User currentUser) {
        return Comment.builder()
                .content(request.getContent())
                .author(userService.getUserEntityById(currentUser.getId()))
                .task(taskService.getTaskEntityById(request.getTaskId()))
                .build();
    }
}
