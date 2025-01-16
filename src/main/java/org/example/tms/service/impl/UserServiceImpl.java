package org.example.tms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.tms.exception.custom.UserNotFoundException;
import org.example.tms.model.User;
import org.example.tms.repository.UserRepository;
import org.example.tms.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "User with this email: " + email + " was not found in database"));
    }

    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }
}
