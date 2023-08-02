package com.banking.app.bankingapp.repo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;
import java.util.Optional;

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
import com.banking.app.bankingapp.domain.repo.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionRepositoryServiceTests {
    @InjectMocks
    private TransactionRepositoryService transactionRepositoryService;
    @Mock
    private TransactionRepository transactionRepository;

    Transaction t1 = Transaction.builder().id("123").amount(10).category(TransactionType.RECHARGE).walletId("123ram")
            .build();

    @Test
    public void testAdd() {
        Mockito.when(transactionRepository.insert(t1)).thenReturn(t1);
        Transaction transaction = transactionRepositoryService.add(t1);
        Assert.assertEquals(t1, transaction);
    }

    @Test
    public void testGet() {
        Mockito.when(transactionRepository.findById(anyString())).thenReturn(Optional.of(t1));
        Transaction transaction = transactionRepositoryService.get(anyString());
        Assert.assertEquals(t1, transaction);
    }

    @Test
    public void testGetALL() {
        Mockito.when(transactionRepository.findAll()).thenReturn(List.of(t1));
        List<Transaction> transaction = transactionRepositoryService.getAll();
        Assert.assertEquals(1, transaction.size());
    }

    @Test
    public void testGetAllByWalletId() {
        Mockito.when(transactionRepository.findByWalletId(anyString())).thenReturn(List.of(t1));
        List<Transaction> transaction = transactionRepositoryService.getAllByWalletId(anyString());
        Assert.assertEquals(1, transaction.size());
    }

    @Test
    public void testGetAllRechargeTransactions() {
        Mockito.when(transactionRepository.findByWalletIdAndCategory(anyString(), any())).thenReturn(List.of(t1));
        List<Transaction> transaction = transactionRepositoryService.getAllRechargeTransactions(anyString(), any());
        Assert.assertEquals(1, transaction.size());
    }
}
