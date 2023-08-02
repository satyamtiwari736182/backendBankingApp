package com.banking.app.bankingapp.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.banking.app.bankingapp.domain.entity.User;
import com.banking.app.bankingapp.domain.repo.UserRepository;
import com.banking.app.bankingapp.error.exception.UserNotFoundException;

@Service
public class UserInfoService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = userRepository.findByName(username);
        if (user == null)
            throw new UserNotFoundException("invalid user credentials");

        return user.map(UserInfoDetails::new).orElseThrow(() -> new UserNotFoundException("invalid user"));

    }
}
