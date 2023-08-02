package com.banking.app.bankingapp.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.app.bankingapp.domain.entity.User;
import com.banking.app.bankingapp.domain.repo.UserRepository;

@Service
public class UserRepositoryService {
    @Autowired
    private UserRepository userRepository;

    public User add(User user) {
        return userRepository.insert(user);
    }

    public User get(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.isPresent() ? user.get() : null;
    }

    public User getUserByName(String name) {
        Optional<User> user = userRepository.findByName(name);
        System.out.println("userName....." + user);
        return user.isPresent() ? user.get() : null;
    }

    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        System.out.println("userEmail....." + user);
        return user.isPresent() ? user.get() : null;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
