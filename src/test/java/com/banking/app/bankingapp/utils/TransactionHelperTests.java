package com.banking.app.bankingapp.utils;

import static org.mockito.ArgumentMatchers.anyString;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banking.app.bankingapp.domain.entity.Transaction;
import com.banking.app.bankingapp.domain.entity.Wallet;
import com.banking.app.bankingapp.domain.enums.TransactionType;
import com.banking.app.bankingapp.dto.TransferDto;
import com.banking.app.bankingapp.error.exception.InsufficientBalanceException;
import com.banking.app.bankingapp.error.exception.WalletNotFoundException;
import com.banking.app.bankingapp.services.WalletService;

@ExtendWith(MockitoExtension.class)
public class TransactionHelperTests {
    @InjectMocks
    private TransactionHelper transactionHelper;
    @Mock
    private WalletService walletService;
    Wallet w1 = Wallet.builder().userId("1").walletId("123ram").balance(100).build();
    Transaction t1 = Transaction.builder().id("123").amount(10).category(TransactionType.RECHARGE).walletId("123ram")
            .build();

    @Test
    public void testTransactionHelper_Success() {
        Mockito.when(walletService.getWallet(anyString())).thenReturn(w1);
        Transaction transaction = transactionHelper.ValidateTransaction(t1, true);
        Transaction transaction2 = transactionHelper.ValidateTransaction(t1, false);
        Assert.assertEquals(t1, transaction);
        Assert.assertEquals(t1, transaction2);

        TransferDto td1 = TransferDto.builder().senderWalletId("123ram").receiverWalletId("123ram").amount(5).build();
        transactionHelper.ValidateTransfer(td1);
    }

    @Test
    public void testTransactionHelper_Failure_case_1() {
        Mockito.when(walletService.getWallet(anyString())).thenReturn(null);
        WalletNotFoundException thrown = Assert.assertThrows(WalletNotFoundException.class, () -> {
            transactionHelper.ValidateTransaction(t1, true);
        });
        Assert.assertEquals("No wallet found with wallitId: 123ram", thrown.getMessage());
    }

    @Test
    public void testTransactionHelper_Failure_case_2() {
        w1.setBalance(0);
        Mockito.when(walletService.getWallet("123ram")).thenReturn(w1);
        InsufficientBalanceException thrown = Assert.assertThrows(InsufficientBalanceException.class, () -> {
            transactionHelper.ValidateTransaction(t1, false);
        });
        Assert.assertEquals("Insufficient balance!!", thrown.getMessage());
    }
}
