package org.example.tms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.tms.annotation.ExecutionTime;
import org.example.tms.dto.responses.UserResponseDto;
import org.example.tms.exception.custom.UserNotFoundException;
import org.example.tms.mapper.UserMapper;
import org.example.tms.model.User;
import org.example.tms.repository.UserRepository;
import org.example.tms.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of the {@link UserService} for handling User-related operations.
 * Provides methods to save a User, retrieve Users by ID or email and map entities to DTOs.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    /**
     * Saves a User to the repository.
     *
     * @param user the {@link User} to save
     */
    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * Retrieves a {@link User} entity by its ID.
     *
     * @param id the UUID of the User
     * @return the {@link User} entity
     * @throws UserNotFoundException if no User is found with the provided ID
     */
    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public User getUserEntityById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    /**
     * Retrieves a {@link User} entity by email.
     *
     * @param email the email of the User
     * @return the {@link User} entity
     * @throws UserNotFoundException if no User is found with the provided email
     */
    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "User with this email: " + email + " was not found in database"));
    }

    /**
     * Retrieves a {@link UserResponseDto} by User ID.
     *
     * @param id the UUID of the User
     * @return the corresponding {@link UserResponseDto}
     * @throws UserNotFoundException if no User is found with the provided ID
     */
    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toUserResponseDto)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }
}
