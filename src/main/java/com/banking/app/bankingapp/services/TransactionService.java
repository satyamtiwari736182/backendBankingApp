package com.banking.app.bankingapp.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.app.bankingapp.domain.TransactionRepositoryService;
import com.banking.app.bankingapp.domain.entity.Transaction;
import com.banking.app.bankingapp.domain.enums.TransactionType;
import com.banking.app.bankingapp.dto.TransferDto;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepositoryService transactionRepositoryService;

    public Transaction addTransaction(Transaction transaction) {
        return transactionRepositoryService.add(transaction);
    }

    public List<Transaction> getAllUserTransaction(String walletId) {
        return transactionRepositoryService.getAllByWalletId(walletId);
    }

    public List<Transaction> getAllTransaction() {
        return transactionRepositoryService.getAll();
    }

    public List<Transaction> getRewards(String walletId, TransactionType category) {
        return transactionRepositoryService.getAllRechargeTransactions(walletId, category);
    }

}
