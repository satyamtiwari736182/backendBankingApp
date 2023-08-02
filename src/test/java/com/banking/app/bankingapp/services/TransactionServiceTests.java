package com.banking.app.bankingapp.services;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banking.app.bankingapp.domain.TransactionRepositoryService;
import com.banking.app.bankingapp.domain.entity.Transaction;
import com.banking.app.bankingapp.domain.enums.TransactionType;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepositoryService transactionRepositoryService;

    Transaction t1 = Transaction.builder().id("123").amount(10).category(TransactionType.RECHARGE).walletId("123ram")
            .build();

    @Test
    public void testAddTransaction() {
        Mockito.when(transactionRepositoryService.add(t1)).thenReturn(t1);
        Transaction transaction = transactionService.addTransaction(t1);
        Assert.assertEquals(t1.getWalletId(), transaction.getWalletId());
    }

    @Test
    public void testGetAllUserTransaction() {
        Mockito.when(transactionRepositoryService.getAllByWalletId("123ram")).thenReturn(List.of(t1));
        List<Transaction> transaction = transactionService.getAllUserTransaction("123ram");
        Assert.assertEquals(1, transaction.size());
    }

    @Test
    public void testGetAllTransaction() {
        Mockito.when(transactionRepositoryService.getAll()).thenReturn(List.of(t1));
        List<Transaction> transaction = transactionService.getAllTransaction();
        Assert.assertEquals(1, transaction.size());
    }

    @Test
    public void testgetRewards() {
        Mockito.when(transactionRepositoryService.getAllRechargeTransactions("123ram",
                TransactionType.RECHARGE)).thenReturn(List.of(t1));
        List<Transaction> transaction = transactionService.getRewards("123ram",
                TransactionType.RECHARGE);
        Assert.assertEquals(1, transaction.size());
    }

}
