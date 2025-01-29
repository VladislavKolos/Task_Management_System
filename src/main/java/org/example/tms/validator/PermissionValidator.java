package org.example.tms.validator;

import org.example.tms.exception.PermissionDeniedException;
import org.example.tms.model.Comment;
import org.example.tms.model.Task;
import org.example.tms.model.User;
import org.example.tms.model.enums.UserRole;
import org.springframework.stereotype.Component;

import static org.example.tms.exception.PermissionDeniedException.ErrorType;

/**
 * Validator for checking User permissions related to tasks and comments.
 * This class contains methods to validate whether a User has appropriate permissions
 * to perform actions such as modifying or commenting on tasks.
 */
@Component
public class PermissionValidator {

    /**
     * Validates if the provided user has permission to work with the specified task.
     * Only Users with the role of {@link UserRole#ROLE_ADMIN} or Users assigned to the task
     * are allowed to interact with it.
     *
     * @param user the User whose permissions are being validated
     * @param task the task to check the user's assignment against
     * @throws PermissionDeniedException if the User is not assigned to the task and is not an admin
     */
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
            throw new PermissionDeniedException(ErrorType.USER_NOT_ASSIGNEE_OF_TASK, user.getId());
        }
    }

    /**
     * Validates if the User has permission to delete or modify a comment.
     * The User must either be the author of the comment or an admin to perform such actions.
     *
     * @param user    the User whose permissions are being validated
     * @param comment the comment to check the User's ownership against
     * @throws PermissionDeniedException if the User is neither the author of the comment nor an admin
     */
    public void validateCommentOwnershipOrAdmin(User user, Comment comment) {
        boolean isAuthor = comment.getAuthor()
                .getId()
                .equals(user.getId());
        boolean isAdmin = user.getRole() == UserRole.ROLE_ADMIN;

        if (!isAuthor && !isAdmin) {
            throw new PermissionDeniedException(ErrorType.USER_CANNOT_DELETE_COMMENT, user.getId());
        }
    }
}
