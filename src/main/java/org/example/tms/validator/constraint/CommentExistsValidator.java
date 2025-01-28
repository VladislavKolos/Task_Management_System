package org.example.tms.validator.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.tms.repository.CommentRepository;
import org.example.tms.validator.constraint.annotation.CommentExists;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Validator for checking the existence of a comment based on its ID.
 * This class validates if the provided comment ID exists in the repository.
 * Implements the {@link ConstraintValidator} interface to validate the comment ID.
 */
@Component
@RequiredArgsConstructor
public class CommentExistsValidator implements ConstraintValidator<CommentExists, UUID> {
    private final CommentRepository commentRepository;

    /**
     * Validates if the comment ID exists in the database.
     * Returns {@code false} if the ID is {@code null} or if the comment does not exist in the repository.
     *
     * @param commentId                  the ID of the comment to validate
     * @param constraintValidatorContext the context in which the constraint is being evaluated
     * @return {@code true} if the comment with the given ID exists, {@code false} otherwise
     */
    @Override
    public boolean isValid(UUID commentId, ConstraintValidatorContext constraintValidatorContext) {
        if (commentId == null) {
            return false;
        }
        return commentRepository.existsById(commentId);
    }
}
