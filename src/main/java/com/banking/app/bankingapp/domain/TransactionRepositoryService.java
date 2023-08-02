package com.banking.app.bankingapp.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.app.bankingapp.domain.entity.Transaction;
import com.banking.app.bankingapp.domain.enums.TransactionType;
import com.banking.app.bankingapp.domain.repo.TransactionRepository;

@Service
public class TransactionRepositoryService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction add(Transaction transaction) {
        return transactionRepository.insert(transaction);
    }

    public Transaction get(String id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        return transaction.isPresent() ? transaction.get() : null;
    }

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getAllByWalletId(String walletId) {
        return transactionRepository.findByWalletId(walletId);
    }

    public List<Transaction> getAllRechargeTransactions(String walletId, TransactionType category) {
        return transactionRepository.findByWalletIdAndCategory(walletId, category);
    }
}
