package com.banking.app.bankingapp.services;

import java.time.LocalDateTime;
import java.util.Formatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.app.bankingapp.domain.WalletRepositoryService;
import com.banking.app.bankingapp.domain.entity.Transaction;
import com.banking.app.bankingapp.domain.entity.Wallet;
import com.banking.app.bankingapp.domain.enums.TransactionType;
import com.banking.app.bankingapp.dto.TransferDto;
import com.banking.app.bankingapp.error.exception.DuplicateWalletException;
import com.banking.app.bankingapp.error.exception.WalletNotFoundException;
import com.banking.app.bankingapp.utils.TransactionHelper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {
    @Autowired
    private WalletRepositoryService walletRepositoryService;
    @Autowired
    private TransactionHelper transactionHelper;

    @Autowired
    private TransactionService transactionService;

    public Wallet createWallet(Wallet wallet) {
        if (walletRepositoryService.getUserWallet(wallet.getUserId()) != null)
            throw new DuplicateWalletException("Wallet for userId:" + wallet.getUserId()
                    + " already exist");
        if (walletRepositoryService.get(wallet.getWalletId()) != null)
            throw new DuplicateWalletException("invalid walletId");
        return walletRepositoryService.add(wallet);
    }

    public Wallet getUserWallet(String id) {
        Wallet wallet = walletRepositoryService.getUserWallet(id);
        if (wallet == null)
            throw new WalletNotFoundException("No wallet found for user with userId: " + id);
        return wallet;
    }

    public Wallet getWallet(String id) {
        Wallet wallet = walletRepositoryService.get(id);
        if (wallet == null)
            throw new WalletNotFoundException("No wallet found with walletId: " + id);
        return wallet;
    }

    public List<Wallet> getAllWallet() {
        return walletRepositoryService.getAll();
    }

    public Wallet updateWallet(Wallet wallet) {
        if (walletRepositoryService.get(wallet.getWalletId()) == null)
            throw new WalletNotFoundException("No wallet found with walletId: " + wallet.getWalletId());
        return walletRepositoryService.update(wallet);
    }

    public double roundOff(Double amt) {
        return Double.parseDouble(String.format("%.2f", amt));
    }

    @Transactional
    public Transaction updateWalletTransaction(Transaction transaction, boolean isRecharge) {

        Wallet wallet = this.getWallet(transaction.getWalletId());
        if (isRecharge) {
            wallet.setBalance(this.roundOff(transaction.getAmount() + transaction.getCashBack() + wallet.getBalance()));
            wallet.setCashBack(this.roundOff(transaction.getCashBack() + wallet.getCashBack()));
        } else
            wallet.setBalance(this.roundOff(wallet.getBalance() - transaction.getAmount()));
        transaction = transactionService.addTransaction(transaction);

        List<String> walletTransaction = wallet.getTransaction();
        walletTransaction.add(transaction.getId());
        wallet.setTransaction(walletTransaction);
        this.updateWallet(wallet);

        return transaction;
    }

    @Transactional
    public Transaction transfer(TransferDto transaction) {
        System.out.println(transaction + "................");
        transactionHelper.ValidateTransfer(transaction);

        Wallet senderWallet = this.getWallet(transaction.getSenderWalletId());
        Wallet receiverWallet = this.getWallet(transaction.getReceiverWalletId());

        senderWallet.setBalance(this.roundOff(senderWallet.getBalance() - transaction.getAmount()));
        Transaction newTransaction = Transaction.builder()
                .amount(this.roundOff(transaction.getAmount()))
                .category(TransactionType.DEBIT)
                .walletId(transaction.getSenderWalletId())
                .tranferWalletId(transaction.getReceiverWalletId())
                .build();
        Transaction transactionData = transactionService.addTransaction(newTransaction);
        List<String> walletTransaction = senderWallet.getTransaction();
        walletTransaction.add(transactionData.getId());
        senderWallet.setTransaction(walletTransaction);
        this.updateWallet(senderWallet);

        receiverWallet.setBalance(this.roundOff(receiverWallet.getBalance() +
                transaction.getAmount()));
        Transaction newTransaction2 = Transaction.builder()
                .amount(this.roundOff(transaction.getAmount()))
                .category(TransactionType.CREDIT)
                .walletId(transaction.getReceiverWalletId())
                .tranferWalletId(transaction.getSenderWalletId())
                // .dateTime(LocalDateTime.now())
                .build();
        transactionData = transactionService.addTransaction(newTransaction2);

        walletTransaction = receiverWallet.getTransaction();
        walletTransaction.add(transactionData.getId());
        receiverWallet.setTransaction(walletTransaction);
        this.updateWallet(receiverWallet);

        return newTransaction;
    }

    public Transaction rechageWallet(Transaction transaction) {
        transaction = transactionHelper.ValidateTransaction(transaction, true);
        return this.updateWalletTransaction(transaction, true);

    }

    public Transaction transferWallet(Transaction transaction) {
        transaction = transactionHelper.ValidateTransaction(transaction, false);
        return this.updateWalletTransaction(transaction, false);
    }

}
