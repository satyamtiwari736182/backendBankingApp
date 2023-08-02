package com.banking.app.bankingapp.domain.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.banking.app.bankingapp.domain.entity.Wallet;

@Component
public interface WalletRepository extends MongoRepository<Wallet, String> {
    public Optional<Wallet> findByWalletId(String walletId);

    public Optional<Wallet> findByUserId(String walletId);
}
