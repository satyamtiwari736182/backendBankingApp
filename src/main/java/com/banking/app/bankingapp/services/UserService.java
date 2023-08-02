package com.banking.app.bankingapp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.banking.app.bankingapp.domain.UserRepositoryService;
import com.banking.app.bankingapp.domain.entity.User;
import com.banking.app.bankingapp.error.exception.UserNotFoundException;

@Service
public class UserService {
    @Autowired
    private UserRepositoryService userRepositoryService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User addUser(User user) {
        if (userRepositoryService.getUserByName(user.getName()) != null
                || userRepositoryService.getUserByEmail(user.getEmail()) != null)
            throw new UserNotFoundException("Invalid username or email!!");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepositoryService.add(user);
    }

    public User getUser(String id) {
        User user = userRepositoryService.get(id);
        if (user == null)
            throw new UserNotFoundException("Invalid user with userId:" + id);
        return user;
    }

    public User getUserByName(String name) {
        User user = userRepositoryService.getUserByName(name);
        if (user == null)
            throw new UserNotFoundException("Invalid user with username:" + name);
        return user;
    }

    public List<User> getAllUser() {
        return userRepositoryService.getAll();
    }
}
