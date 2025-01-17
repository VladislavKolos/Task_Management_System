package org.example.tms.util;

import lombok.extern.slf4j.Slf4j;
import org.example.tms.exception.custom.NullUserObjectException;
import org.example.tms.exception.custom.UnauthenticatedClientAccessException;
import org.example.tms.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class CurrentUserUtil {
    private CurrentUserUtil() {
    }

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            log.error("Authentication or User is null, unable to retrieve User ID.");
            throw new UnauthenticatedClientAccessException(
                    "Unable to retrieve current User ID, User is not authenticated.");
        }

        User user = (User) authentication.getPrincipal();

        if (user == null) {
            log.error("User is null, unable to retrieve User ID.");
            throw new NullUserObjectException("Unable to retrieve current User ID, User object is null.");
        }

        log.info("Retrieved current User, ID: {}", user.getId());

        return user;
    }
}
