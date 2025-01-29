package org.example.tms.validator.custom;

import org.example.tms.repository.CommentRepository;
import org.example.tms.validator.constraint.CommentExistsValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentExistsValidatorTest {

    @Mock
    private CommentRepository commentRepository;

    private CommentExistsValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new CommentExistsValidator(commentRepository);
    }

    @Test
    public void testIsValid_ShouldReturnFalse_WhenCommentIdIsNull() {
        boolean isValid = validator.isValid(null, null);

        assertFalse(isValid, "Expected false when commentId is null");
        verifyNoInteractions(commentRepository);
    }

    @Test
    public void testIsValid_ShouldReturnTrue_WhenCommentExists() {
        UUID commentId = UUID.randomUUID();
        when(commentRepository.existsById(commentId))
                .thenReturn(true);

        boolean isValid = validator.isValid(commentId, null);

        assertTrue(isValid, "Expected true when the comment exists in the repository");
        verify(commentRepository)
                .existsById(commentId);
    }

    @Test
    public void testIsValid_ShouldReturnFalse_WhenCommentDoesNotExist() {
        UUID commentId = UUID.randomUUID();
        when(commentRepository.existsById(commentId))
                .thenReturn(false);

        boolean isValid = validator.isValid(commentId, null);

        assertFalse(isValid, "Expected false when the comment does not exist in the repository");
        verify(commentRepository)
                .existsById(commentId);
    }
}
