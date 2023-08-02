package com.banking.app.bankingapp.domain.repo;

import org.springframework.stereotype.Component;

import com.banking.app.bankingapp.domain.entity.User;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

@Component
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);

    void removeByName(String name);
}
