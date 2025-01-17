package org.example.tms.mapper;

import org.example.tms.dto.requests.create.CreateTaskRequestDto;
import org.example.tms.dto.responses.TaskResponseDto;
import org.example.tms.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {TaskAssigneeMapper.class,
        CommentMapper.class})
@Component
public interface TaskMapper {

    @Mapping(source = "author", target = "author")
    @Mapping(source = "taskAssignees", target = "taskAssignees")
    @Mapping(source = "comments", target = "comments")
    TaskResponseDto toTaskResponseDto(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "authorId", target = "author.id")
    Task toTaskForCreate(CreateTaskRequestDto taskRequestDto);
}
