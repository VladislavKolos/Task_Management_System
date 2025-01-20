package org.example.tms.validator.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.tms.annotation.custom.TaskExists;
import org.example.tms.repository.TaskRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Validator for checking the existence of a task based on its ID.
 * This class validates if the provided task ID exists in the repository.
 * Implements the {@link ConstraintValidator} interface to validate the task ID.
 */
@Component
@RequiredArgsConstructor
public class TaskExistsValidator implements ConstraintValidator<TaskExists, UUID> {
    private final TaskRepository taskRepository;

    /**
     * Validates if the task ID exists in the database.
     * Returns {@code false} if the ID is {@code null} or if the task does not exist in the repository.
     *
     * @param taskId                     the ID of the task to validate
     * @param constraintValidatorContext the context in which the constraint is being evaluated
     * @return {@code true} if the task with the given ID exists, {@code false} otherwise
     */
    @Override
    public boolean isValid(UUID taskId, ConstraintValidatorContext constraintValidatorContext) {
        if (taskId == null) {
            return false;
        }
        return taskRepository.existsById(taskId);
    }
}
