package org.example.tms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Comments", description = "Endpoints for managing comments related to tasks")
@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final UriService uriService;
    private final CommentService commentService;

    @Operation(
            summary = "Get comment by ID",
            description = "Fetches a comment by its unique ID.",
            tags = {"Comments"}
    )
    @Parameter(
            name = "id",
            description = "Unique identifier of the comment",
            required = true,
            example = "123e4567-e89b-12d3-a456-426614174000"
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto getCommentById(@PathVariable @CommentExists UUID id) {
        CommentResponseDto response = commentService.getCommentById(id);
        log.info("Comment with ID: {} fetched successfully", id);

        return response;
    }

    @Operation(
            summary = "Get comments by task ID",
            description = "Fetches all comments associated with a specific task.",
            tags = {"Comments"}
    )
    @Parameter(
            name = "taskId",
            description = "Unique identifier of the task",
            required = true,
            example = "123e4567-e89b-12d3-a456-426614174001"
    )
    @Parameter(
            name = "pageable",
            description = "Pagination and sorting information",
            example = "{ \"page\": 0, \"size\": 10, \"sort\": \"task,desc\" }"
    )
    @GetMapping("/task/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<CommentResponseDto> getCommentsByTaskId(
            @PathVariable @TaskExists UUID taskId,
            @PageableDefault(sort = "task", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<CommentResponseDto> response = commentService.getCommentsByTaskId(taskId, pageable);
        log.info("Fetched {} comments for task with ID: {}", response.getTotalElements(), taskId);

        return response;
    }

    @Operation(
            summary = "Add a new comment",
            description = "Adds a new comment to a specific task.",
            tags = {"Comments"}
    )
    @PostMapping
    public ResponseEntity<CommentResponseDto> addComment(@Valid @RequestBody CreateCommentRequestDto request) {
        CommentResponseDto response = commentService.addComment(request);
        log.info("Comment added successfully to task with ID: {}", request.getTaskId());

        String resourceUri = uriService.createCommentUri(response.id());

        return ResponseEntity.created(URI.create(resourceUri))
                .body(response);
    }

    @Operation(
            summary = "Delete a comment",
            description = "Deletes a comment by its unique ID.",
            tags = {"Comments"}
    )
    @Parameter(
            name = "id",
            description = "Unique identifier of the comment",
            required = true,
            example = "123e4567-e89b-12d3-a456-426614174002"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @CommentExists UUID id) {
        commentService.deleteComment(id);
        log.info("Comment with ID: {} deleted successfully", id);
    }
}
