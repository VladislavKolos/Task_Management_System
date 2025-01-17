package org.example.tms.service;

import org.example.tms.dto.requests.create.CreateTaskAssigneeRequestDto;
import org.example.tms.dto.responses.TaskAssigneeResponseDto;
import org.springframework.stereotype.Component;

@Component
public interface TaskAssigneeService {
    TaskAssigneeResponseDto assignTaskToUser(CreateTaskAssigneeRequestDto request);
}
