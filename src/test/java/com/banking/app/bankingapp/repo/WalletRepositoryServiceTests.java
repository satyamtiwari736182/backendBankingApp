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

import com.banking.app.bankingapp.domain.WalletRepositoryService;
import com.banking.app.bankingapp.domain.entity.Wallet;
import com.banking.app.bankingapp.domain.repo.WalletRepository;

@ExtendWith(MockitoExtension.class)
public class WalletRepositoryServiceTests {
    @InjectMocks
    private WalletRepositoryService walletRepositoryService;
    @Mock
    private WalletRepository walletRepository;

    private Wallet w1 = Wallet.builder().userId("1").walletId("123ram").build();

    @Test
    public void testAdd() {
        Mockito.when(walletRepository.insert(w1)).thenReturn(w1);
        Wallet wallet = walletRepositoryService.add(w1);
        Assert.assertEquals(w1, wallet);
    }

    @Test
    public void testGet() {
        Mockito.when(walletRepository.findByWalletId(anyString())).thenReturn(Optional.of(w1));
        Wallet wallet = walletRepositoryService.get(anyString());
        Assert.assertEquals(w1, wallet);
    }

    @Test
    public void testGetAll() {
        Mockito.when(walletRepository.findAll()).thenReturn(List.of(w1));
        List<Wallet> wallet = walletRepositoryService.getAll();
        Assert.assertEquals(1, wallet.size());
    }

    @Test
    public void testGetUserWallet() {
        Mockito.when(walletRepository.findByUserId(anyString())).thenReturn(Optional.of(w1));
        Wallet wallet = walletRepositoryService.getUserWallet(anyString());
        Assert.assertEquals(w1, wallet);
    }

    @Test
    public void testUpdate() {
        Mockito.when(walletRepository.save(any())).thenReturn(w1);
        Wallet wallet = walletRepositoryService.update(any());
        Assert.assertEquals(w1, wallet);
    }
}
