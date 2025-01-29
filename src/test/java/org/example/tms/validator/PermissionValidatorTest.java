package org.example.tms.validator;

import org.example.tms.exception.PermissionDeniedException;
import org.example.tms.model.Comment;
import org.example.tms.model.Task;
import org.example.tms.model.TaskAssignee;
import org.example.tms.model.User;
import org.example.tms.model.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PermissionValidatorTest {
    private PermissionValidator permissionValidator;

    private User adminUser;
    private User regularUser;
    private Task task;
    private Comment comment;
    private TaskAssignee taskAssignee;

    @BeforeEach
    public void setUp() {
        permissionValidator = new PermissionValidator();

        adminUser = User.builder()
                .id(UUID.randomUUID())
                .role(UserRole.ROLE_ADMIN)
                .build();

        regularUser = User.builder()
                .id(UUID.randomUUID())
                .role(UserRole.ROLE_USER)
                .build();

        User assignee = User.builder()
                .id(UUID.randomUUID())
                .role(UserRole.ROLE_USER)
                .build();

        task = Task.builder()
                .id(UUID.randomUUID())
                .title("Sample Task")
                .taskAssignees(new ArrayList<>())
                .build();

        taskAssignee = TaskAssignee.builder()
                .task(task)
                .assignee(assignee)
                .assignedAt(LocalDateTime.now())
                .build();

        task.getTaskAssignees()
                .add(taskAssignee);

        comment = Comment.builder()
                .id(UUID.randomUUID())
                .author(regularUser)
                .task(task)
                .build();
    }

    @Test
    public void testValidateAssigneePermission_ShouldPassForAdmin() {
        assertDoesNotThrow(() -> permissionValidator.validateAssigneePermission(adminUser, task));
    }

    @Test
    public void testValidateAssigneePermission_ShouldPassForAssignee() {
        assertDoesNotThrow(() -> permissionValidator.validateAssigneePermission(taskAssignee.getAssignee(), task));
    }

    @Test
    public void testValidateAssigneePermission_ShouldThrowForNonAssignee() {
        assertThrows(PermissionDeniedException.class,
                () -> permissionValidator.validateAssigneePermission(regularUser, task));
    }

    @Test
    public void testValidateCommentOwnershipOrAdmin_ShouldPassForAdmin() {
        assertDoesNotThrow(() -> permissionValidator.validateCommentOwnershipOrAdmin(adminUser, comment));
    }

    @Test
    public void testValidateCommentOwnershipOrAdmin_ShouldPassForAuthor() {
        assertDoesNotThrow(() -> permissionValidator.validateCommentOwnershipOrAdmin(regularUser, comment));
    }

    @Test
    public void testValidateCommentOwnershipOrAdmin_ShouldThrowForNonAuthorNonAdmin() {
        User otherUser = User.builder()
                .id(UUID.randomUUID())
                .role(UserRole.ROLE_USER)
                .build();

        assertThrows(PermissionDeniedException.class,
                () -> permissionValidator.validateCommentOwnershipOrAdmin(otherUser, comment));
    }
}
