package org.example.tms.mapper;

import org.example.tms.dto.responses.TaskAssigneeResponseDto;
import org.example.tms.model.TaskAssignee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
@Component
public interface TaskAssigneeMapper {

    @Mapping(target = "task", ignore = true)
    @Mapping(source = "assignee", target = "assignee")
    TaskAssigneeResponseDto toTaskAssigneeResponseDto(TaskAssignee taskAssignee);
}
