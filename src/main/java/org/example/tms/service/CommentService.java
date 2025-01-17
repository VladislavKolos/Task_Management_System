package org.example.tms.service;

import org.example.tms.dto.requests.create.CreateCommentRequestDto;
import org.example.tms.dto.responses.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface CommentService {
    CommentResponseDto getCommentById(UUID id);

    Page<CommentResponseDto> getCommentsByTaskId(UUID taskId, Pageable pageable);

    CommentResponseDto addComment(CreateCommentRequestDto request);

    void deleteComment(UUID id);
}
