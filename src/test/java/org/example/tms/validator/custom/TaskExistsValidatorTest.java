package org.example.tms.validator.custom;

import org.example.tms.repository.TaskRepository;
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
public class TaskExistsValidatorTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskExistsValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new TaskExistsValidator(taskRepository);
    }

    @Test
    public void testIsValid_ShouldReturnFalse_WhenTaskIdIsNull() {
        boolean isValid = validator.isValid(null, null);

        assertFalse(isValid);
        verifyNoInteractions(taskRepository);
    }

    @Test
    public void testIsValid_ShouldReturnTrue_WhenTaskExists() {
        UUID taskId = UUID.randomUUID();
        when(taskRepository.existsById(taskId))
                .thenReturn(true);

        boolean isValid = validator.isValid(taskId, null);

        assertTrue(isValid);
        verify(taskRepository)
                .existsById(taskId);
    }

    @Test
    public void testIsValid_ShouldReturnFalse_WhenTaskDoesNotExist() {
        UUID taskId = UUID.randomUUID();
        when(taskRepository.existsById(taskId))
                .thenReturn(false);

        boolean isValid = validator.isValid(taskId, null);

        assertFalse(isValid);
        verify(taskRepository)
                .existsById(taskId);
    }
}
