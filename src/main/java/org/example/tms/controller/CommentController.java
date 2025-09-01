package org.example.tms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.dto.requests.create.CreateCommentRequestDto;
import org.example.tms.dto.responses.CommentResponseDto;
import org.example.tms.service.CommentService;
import org.example.tms.service.UriService;
import org.example.tms.validator.constraint.annotation.CommentExists;
import org.example.tms.validator.constraint.annotation.TaskExists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final UriService uriService;
    private final CommentService commentService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto getCommentById(@PathVariable @CommentExists UUID id) {
        log.info("Incoming request to fetch comment with ID: {}", id);

        CommentResponseDto response = commentService.getCommentById(id);
        log.info("Comment with ID: {} fetched successfully", id);

        return response;
    }

    @GetMapping("/task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<CommentResponseDto> getCommentsByTaskId(
            @PathVariable @TaskExists UUID taskId,
            @PageableDefault(sort = "task", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Incoming request to fetch comments for task with ID: {}. Pageable: {}", taskId, pageable);

        Page<CommentResponseDto> response = commentService.getCommentsByTaskId(taskId, pageable);
        log.info("Fetched {} comments for task with ID: {}", response.getTotalElements(), taskId);

        return response;
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> addComment(@Valid @RequestBody CreateCommentRequestDto request) {
        log.info("Incoming request to add comment to task with ID: {}", request.getTaskId());

        CommentResponseDto response = commentService.addComment(request);
        log.info("Comment added successfully to task with ID: {}", request.getTaskId());

        String resourceUri = uriService.createCommentUri(response.id());
        log.info("Comment resource URI created: {}", resourceUri);

        return ResponseEntity.created(URI.create(resourceUri))
                .body(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @CommentExists UUID id) {
        log.info("Incoming request to delete comment with ID: {}", id);

        commentService.deleteComment(id);
        log.info("Comment with ID: {} deleted successfully", id);
    }
}
