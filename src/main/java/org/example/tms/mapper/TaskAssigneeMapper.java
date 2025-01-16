package org.example.tms.mapper;

import org.example.tms.dto.requests.create.CreateTaskAssigneeRequestDto;
import org.example.tms.dto.responses.TaskAssigneeResponseDto;
import org.example.tms.model.TaskAssignee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TaskMapper.class})
@Component
public interface TaskAssigneeMapper {

    @Mapping(source = "task", target = "task")
    @Mapping(source = "assignee", target = "assignee")
    TaskAssigneeResponseDto toTaskAssigneeResponseDto(TaskAssignee taskAssignee);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "taskId", target = "task.id")
    @Mapping(source = "assigneeId", target = "assignee.id")
    TaskAssignee toTaskAssigneeForCreate(CreateTaskAssigneeRequestDto taskAssigneeRequestDto);
}
