package org.example.tms.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.example.tms.exception.NullUserObjectException;
import org.example.tms.exception.UnauthenticatedClientAccessException;
import org.example.tms.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for retrieving the currently authenticated User from the security context.
 * Provides a method to get the {@link User} object of the currently authenticated User.
 * This class should not be instantiated.
 */
@Slf4j
@UtilityClass
public class CurrentUserUtil {

    /**
     * Retrieves the currently authenticated User from the security context.
     * Throws exceptions if authentication is null or if the User object cannot be found.
     * Logs errors and User retrieval details for debugging.
     *
     * @return the {@link User} object of the currently authenticated User
     * @throws UnauthenticatedClientAccessException if the User is not authenticated
     * @throws NullUserObjectException              if the User object is null
     */
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            log.error("Authentication or User is null, unable to retrieve User ID.");
            throw new UnauthenticatedClientAccessException();
        }

        User user = (User) authentication.getPrincipal();

        if (user == null) {
            log.error("User is null, unable to retrieve User ID.");
            throw new NullUserObjectException();
        }

        log.info("Retrieved current User, ID: {}", user.getId());

        return user;
    }
}
