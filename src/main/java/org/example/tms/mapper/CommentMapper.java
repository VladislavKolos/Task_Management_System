package org.example.tms.mapper;

import org.example.tms.dto.requestdto.creating.CreatingCommentRequestDto;
import org.example.tms.dto.responsedto.CommentResponseDto;
import org.example.tms.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TaskMapper.class})
@Component
public interface CommentMapper {

    @Mapping(source = "task", target = "task")
    @Mapping(source = "author", target = "author")
    CommentResponseDto toCommentResponseDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "taskId", target = "task.id")
    @Mapping(source = "authorId", target = "author.id")
    Comment toCommentForCreate(CreatingCommentRequestDto commentRequestDto);
}
