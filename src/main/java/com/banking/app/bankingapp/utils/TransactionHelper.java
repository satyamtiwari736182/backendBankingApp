package com.banking.app.bankingapp.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.banking.app.bankingapp.domain.entity.Transaction;
import com.banking.app.bankingapp.domain.entity.Wallet;
import com.banking.app.bankingapp.domain.enums.TransactionType;
import com.banking.app.bankingapp.dto.TransferDto;
import com.banking.app.bankingapp.error.exception.InsufficientBalanceException;
import com.banking.app.bankingapp.error.exception.WalletNotFoundException;
import com.banking.app.bankingapp.services.WalletService;

@Component
public class TransactionHelper {
    @Autowired
    private WalletService walletService;

    public Transaction ValidateTransaction(Transaction transaction, boolean isRecharge) {
        // transaction.setAmount(walletService.roundOff(transaction.getAmount()));
        String walletId = transaction.getWalletId();
        Wallet wallet = walletService.getWallet(walletId);
        System.out.println("testing......" + wallet);
        System.out.println("testing......" + transaction);
        if (wallet == null)
            throw new WalletNotFoundException("No wallet found with wallitId: " + walletId);

        if (isRecharge) {
            transaction.setCategory(TransactionType.RECHARGE);
            transaction.setCashBack(walletService.roundOff(transaction.getAmount() * 0.05));
        } else {
            transaction.setCategory(TransactionType.TRANSFER);
            if (wallet.getBalance() < transaction.getAmount())
                throw new InsufficientBalanceException("Insufficient balance!!");
        }
        return transaction;
    }

    public void ValidateTransfer(TransferDto transaction) {
        Wallet senderWallet = walletService.getWallet(transaction.getSenderWalletId());
        Wallet receiverWallet = walletService.getWallet(transaction.getReceiverWalletId());
        if (senderWallet == null)
            throw new WalletNotFoundException("No wallet found with wallitId: " + senderWallet.getWalletId());
        if (receiverWallet == null)
            throw new WalletNotFoundException("No wallet found!!");
        if (senderWallet.getBalance() < transaction.getAmount())
            throw new InsufficientBalanceException("Insufficient balance!!");
    }
}
