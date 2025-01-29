package org.example.tms.service.impl;

import org.example.tms.dto.responses.UserResponseDto;
import org.example.tms.exception.UserNotFoundException;
import org.example.tms.mapper.UserMapper;
import org.example.tms.model.User;
import org.example.tms.model.enums.UserRole;
import org.example.tms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    private static final String EMAIL = "user@example.com";

    private static final User user = User.builder()
            .id(UUID.randomUUID())
            .email(EMAIL)
            .password("password123")
            .role(UserRole.ROLE_USER)
            .createdAt(LocalDateTime.now())
            .build();

    private static final UserResponseDto userResponseDto = UserResponseDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .role(user.getRole())
            .createdAt(user.getCreatedAt())
            .build();

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Test
    public void testSave_ShouldSaveUser() {
        userService.save(user);

        verify(userRepository).save(argThat(savedUser ->
                savedUser.getEmail()
                        .equals(user.getEmail()) &&
                        savedUser.getPassword()
                                .equals(user.getPassword()) &&
                        savedUser.getRole()
                                .equals(user.getRole())));
    }

    @Test
    public void testGetUserEntityById_ShouldReturnUser_WhenUserExists() {
        UUID userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserEntityById(userId);

        assertEquals(user, result);
        verify(userRepository).findById(userId);
    }

    @Test
    public void testGetUserEntityById_ShouldThrowException_WhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getUserEntityById(userId));
        assertEquals("User not found with ID: " + userId, exception.getMessage());
        verify(userRepository).findById(userId);
    }

    @Test
    public void testGetUserByEmail_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        User result = userService.getUserByEmail(EMAIL);

        assertEquals(user, result);
        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    public void testGetUserByEmail_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getUserByEmail(EMAIL));
        assertEquals("User with this email: " + EMAIL + " was not found in database", exception.getMessage());
        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    public void testGetUserById_ShouldReturnUserResponseDto_WhenUserExists() {
        UUID userId = user.getId();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDto(user)).thenReturn(userResponseDto);

        UserResponseDto result = userService.getUserById(userId);

        assertEquals(userResponseDto, result);
        verify(userRepository).findById(userId);
        verify(userMapper).toUserResponseDto(user);
    }

    @Test
    public void testGetUserById_ShouldThrowException_WhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(userId));
        assertEquals("User not found with ID: " + userId, exception.getMessage());
        verify(userRepository).findById(userId);
    }
}