package org.example.tms.validator.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.tms.annotation.custom.CommentExists;
import org.example.tms.repository.CommentRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommentExistsValidator implements ConstraintValidator<CommentExists, UUID> {
    private final CommentRepository commentRepository;

    @Override
    public boolean isValid(UUID commentId, ConstraintValidatorContext constraintValidatorContext) {
        if (commentId == null) {
            return false;
        }
        return commentRepository.existsById(commentId);
    }
}
