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

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public User getUserEntityById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "User with this email: " + email + " was not found in database"));
    }

    @Override
    @ExecutionTime
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toUserResponseDto)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }
}
