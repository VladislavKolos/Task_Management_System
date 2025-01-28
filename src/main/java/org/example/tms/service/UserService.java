package org.example.tms.service;

import org.example.tms.dto.responses.UserResponseDto;
import org.example.tms.model.User;

import java.util.UUID;

public interface UserService {
    User getUserEntityById(UUID id);

    User getUserByEmail(String email);

    void save(User user);

    UserResponseDto getUserById(UUID id);
}
