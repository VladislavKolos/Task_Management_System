package org.example.tms.service;

import org.example.tms.dto.requests.create.CreateTaskAssigneeRequestDto;
import org.example.tms.dto.responses.TaskAssigneeResponseDto;

public interface TaskAssigneeService {
    TaskAssigneeResponseDto assignTaskToUser(CreateTaskAssigneeRequestDto request);
}
