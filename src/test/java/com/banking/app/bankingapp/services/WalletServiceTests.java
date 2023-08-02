package com.banking.app.bankingapp.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banking.app.bankingapp.domain.WalletRepositoryService;
import com.banking.app.bankingapp.domain.entity.Transaction;
import com.banking.app.bankingapp.domain.entity.Wallet;
import com.banking.app.bankingapp.domain.enums.TransactionType;
import com.banking.app.bankingapp.dto.TransferDto;
import com.banking.app.bankingapp.error.exception.DuplicateWalletException;
import com.banking.app.bankingapp.error.exception.WalletNotFoundException;
import com.banking.app.bankingapp.utils.TransactionHelper;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTests {
    @InjectMocks
    private WalletService walletService;
    @Mock
    private WalletService wService;
    @Mock
    private WalletRepositoryService walletRepositoryService;
    @Mock
    private TransactionHelper transactionHelper;
    @Mock
    private TransactionService transactionService;
    Wallet w1 = Wallet.builder().userId("1").walletId("123ram").build();
    Transaction t1 = Transaction.builder().id("1").amount(100).category(TransactionType.TRANSFER).walletId("123ram")
            .build();

    @Test
    public void testCreateWallet_Success() {
        Mockito.when(walletRepositoryService.getUserWallet("1")).thenReturn(null);
        Mockito.when(walletRepositoryService.add(w1)).thenReturn(w1);
        Wallet wallet = walletService.createWallet(w1);
        Assert.assertEquals("123ram", wallet.getWalletId());
    }

    @Test
    public void testCreateWallet_Failure() {
        Mockito.when(walletRepositoryService.getUserWallet("1")).thenReturn(w1);
        DuplicateWalletException thrown = Assert.assertThrows(DuplicateWalletException.class, () -> {
            walletService.createWallet(w1);
        });
        Assert.assertEquals("Wallet for userId:1 already exist", thrown.getMessage());
    }

    @Test
    public void testGetUserWallet_Success() {
        Mockito.when(walletRepositoryService.getUserWallet("1")).thenReturn(w1);
        Wallet wallet = walletService.getUserWallet("1");
        Assert.assertEquals("123ram", wallet.getWalletId());
    }

    @Test
    public void testGetUserWallet_Failure() {
        Mockito.when(walletRepositoryService.getUserWallet("1")).thenReturn(null);
        WalletNotFoundException thrown = Assert.assertThrows(WalletNotFoundException.class, () -> {
            walletService.getUserWallet("1");
        });
        Assert.assertEquals("No wallet found for user with userId: 1", thrown.getMessage());
    }

    @Test
    public void testGetWallet_Success() {
        Mockito.when(walletRepositoryService.get("1")).thenReturn(w1);
        Wallet wallet = walletService.getWallet("1");
        Assert.assertEquals("123ram", wallet.getWalletId());
    }

    @Test
    public void testGetWallet_Failure() {
        Mockito.when(walletRepositoryService.get("1")).thenReturn(null);
        WalletNotFoundException thrown = Assert.assertThrows(WalletNotFoundException.class, () -> {
            walletService.getWallet("1");
        });
        Assert.assertEquals("No wallet found with walletId: 1", thrown.getMessage());
    }

    @Test
    public void testGetAllWallet() {
        Mockito.when(walletRepositoryService.getAll()).thenReturn(List.of(w1));
        List<Wallet> walletList = walletService.getAllWallet();
        Assert.assertEquals(1, walletList.size());
    }

    @Test
    public void testUpdateWallet_Success() {
        Mockito.when(walletRepositoryService.get("123ram")).thenReturn(w1);
        Mockito.when(walletRepositoryService.update(w1)).thenReturn(w1);
        Wallet wallet = walletService.updateWallet(w1);
        Assert.assertEquals("123ram", wallet.getWalletId());
    }

    @Test
    public void testUpdateWallet_Failure() {
        Mockito.lenient().when(walletRepositoryService.get(anyString())).thenReturn(null);
        WalletNotFoundException thrown = Assert.assertThrows(WalletNotFoundException.class, () -> {
            walletService.updateWallet(w1);
        });
        Assert.assertEquals("No wallet found with walletId: 123ram", thrown.getMessage());
    }

    @Test
    public void testRoundOff() {
        double num = walletService.roundOff(12.34545);
        Assert.assertTrue(num == 12.35);
    }

    @Test
    public void testUpdateWalletTransaction() {
        Mockito.when(walletRepositoryService.get(anyString())).thenReturn(w1);
        Mockito.when(transactionService.addTransaction(t1)).thenReturn(t1);

        Transaction transaction = walletService.updateWalletTransaction(t1, false);
        Assert.assertEquals(w1.getWalletId(), transaction.getWalletId());
    }

    @Test
    public void testRechageWallet() {

        Mockito.when(transactionHelper.ValidateTransaction(t1, true)).thenReturn(t1);
        Mockito.when(walletRepositoryService.get(anyString())).thenReturn(w1);
        Mockito.when(transactionService.addTransaction(t1)).thenReturn(t1);

        Transaction transaction = walletService.rechageWallet(t1);
        Assert.assertEquals(t1, transaction);
    }

    @Test
    public void testTransferWallet() {
        Mockito.when(transactionHelper.ValidateTransaction(t1, false)).thenReturn(t1);
        Mockito.when(walletRepositoryService.get(anyString())).thenReturn(w1);
        Mockito.when(transactionService.addTransaction(t1)).thenReturn(t1);

        Transaction transaction = walletService.transferWallet(t1);
        Assert.assertEquals(t1, transaction);
    }

    @Test
    public void testTransfer() {
        Mockito.lenient().when(transactionHelper.ValidateTransaction(t1, false)).thenReturn(t1);
        Mockito.when(walletRepositoryService.get("123ram")).thenReturn(w1);
        Mockito.when(transactionService.addTransaction(any())).thenReturn(t1);

        TransferDto td1 = TransferDto.builder().senderWalletId("123ram").receiverWalletId("123ram").amount(5).build();
        Transaction transaction = walletService.transfer(td1);
        Assert.assertEquals(TransactionType.DEBIT, transaction.getCategory());

    }

}
