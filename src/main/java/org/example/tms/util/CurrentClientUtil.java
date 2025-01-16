package org.example.tms.util;

import lombok.extern.slf4j.Slf4j;
import org.example.tms.exception.custom.NullUserObjectException;
import org.example.tms.exception.custom.UnauthenticatedClientAccessException;
import org.example.tms.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

@Slf4j
public class CurrentClientUtil {
    private CurrentClientUtil() {
    }

    public static UUID getCurrentClientId() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            log.error("Authentication or user is null, unable to retrieve client ID.");
            throw new UnauthenticatedClientAccessException(
                    "Unable to retrieve current client ID, user is not authenticated.");
        }

        User user = (User) authentication.getPrincipal();

        if (user == null) {
            log.error("User is null, unable to retrieve client ID.");
            throw new NullUserObjectException("Unable to retrieve current client ID, user object is null.");
        }

        log.info("Retrieved current client ID: {}", user.getId());

        return user.getId();
    }
}
