package org.example.tms.validator.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.tms.annotation.custom.TaskExists;
import org.example.tms.repository.TaskRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskExistsValidator implements ConstraintValidator<TaskExists, UUID> {
    private final TaskRepository taskRepository;

    @Override
    public boolean isValid(UUID taskId, ConstraintValidatorContext constraintValidatorContext) {
        if (taskId == null) {
            return false;
        }
        return taskRepository.existsById(taskId);
    }
}
