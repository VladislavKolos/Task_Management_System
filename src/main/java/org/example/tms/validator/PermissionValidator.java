package org.example.tms.validator;

import org.example.tms.exception.custom.PermissionDeniedException;
import org.example.tms.model.Comment;
import org.example.tms.model.Task;
import org.example.tms.model.User;
import org.example.tms.model.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class PermissionValidator {
    public void validateAssigneePermission(User user, Task task) {
        if (user.getRole() == UserRole.ROLE_ADMIN) {
            return;
        }

        boolean isAssignee = task.getTaskAssignees()
                .stream()
                .anyMatch(taskAssignee -> taskAssignee.getAssignee()
                        .getId()
                        .equals(user.getId()));

        if (!isAssignee) {
            throw new PermissionDeniedException("User with ID: " + user.getId() + " - is not an assignee of the task.");
        }
    }

    public void validateCommentOwnershipOrAdmin(User user, Comment comment) {
        boolean isAuthor = comment.getAuthor()
                .getId()
                .equals(user.getId());
        boolean isAdmin = user.getRole() == UserRole.ROLE_ADMIN;

        if (!isAuthor && !isAdmin) {
            throw new PermissionDeniedException(
                    "User with ID: " + user.getId() + " - does not have permission to delete this comment.");
        }
    }
}
