package org.example.tms.mapper;

import org.example.tms.dto.responses.CommentResponseDto;
import org.example.tms.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
@Component
public interface CommentMapper {

    @Mapping(target = "task", ignore = true)
    @Mapping(source = "author", target = "author")
    CommentResponseDto toCommentResponseDto(Comment comment);
}
