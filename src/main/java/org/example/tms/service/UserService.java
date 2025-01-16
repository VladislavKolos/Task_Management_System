package org.example.tms.service;

import org.example.tms.model.User;
import org.springframework.stereotype.Component;

@Component
public interface UserService {
    User getUserByEmail(String email);

    void save(User user);
}
