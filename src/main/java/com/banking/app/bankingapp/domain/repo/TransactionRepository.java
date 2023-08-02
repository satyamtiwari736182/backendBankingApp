package com.banking.app.bankingapp.domain.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import com.banking.app.bankingapp.domain.entity.Transaction;
import com.banking.app.bankingapp.domain.enums.TransactionType;

@Component
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByWalletId(String walletId);

    List<Transaction> findByWalletIdAndCategory(String walletId, TransactionType category);
}
