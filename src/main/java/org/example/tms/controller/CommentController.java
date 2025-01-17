package org.example.tms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.annotation.custom.CommentExists;
import org.example.tms.annotation.custom.TaskExists;
import org.example.tms.dto.requests.create.CreateCommentRequestDto;
import org.example.tms.dto.responses.CommentResponseDto;
import org.example.tms.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable @CommentExists UUID id) {
        CommentResponseDto response = commentService.getCommentById(id);
        log.info("Comment with ID: {} fetched successfully", id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByTaskId(
            @PathVariable @TaskExists UUID taskId,
            @PageableDefault(sort = "task", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<CommentResponseDto> comments = commentService.getCommentsByTaskId(taskId, pageable);
        log.info("Fetched {} comments for task with ID: {}", comments.getTotalElements(), taskId);

        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> addComment(@Valid @RequestBody CreateCommentRequestDto request) {
        CommentResponseDto response = commentService.addComment(request);
        log.info("Comment added successfully to task with ID: {}", request.getTaskId());

        return ResponseEntity.created(URI.create("/api/comments/" + response.getId()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable @CommentExists UUID id) {
        commentService.deleteComment(id);
        log.info("Comment with ID: {} deleted successfully", id);

        return ResponseEntity.noContent()
                .build();
    }
}
