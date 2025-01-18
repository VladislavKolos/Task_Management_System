package org.example.tms.util;

import org.example.tms.exception.custom.UnauthenticatedClientAccessException;
import org.example.tms.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CurrentUserUtilTest {

    @Test
    public void testGetCurrentUser_Success() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User result = CurrentUserUtil.getCurrentUser();

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(securityContext).getAuthentication();
    }

    @Test
    public void testGetCurrentUser_NoAuthentication() {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        UnauthenticatedClientAccessException exception = assertThrows(UnauthenticatedClientAccessException.class,
                CurrentUserUtil::getCurrentUser);
        assertEquals("Unable to retrieve current User ID, User is not authenticated.", exception.getMessage());
    }
}