package com.banking.app.bankingapp.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.app.bankingapp.domain.entity.Wallet;
import com.banking.app.bankingapp.domain.repo.WalletRepository;

@Service
public class WalletRepositoryService {
    @Autowired
    private WalletRepository walletRepository;

    public Wallet add(Wallet wallet) {
        return walletRepository.insert(wallet);
    }

    public Wallet get(String id) {
        Optional<Wallet> wallet = walletRepository.findByWalletId(id);
        return wallet.isPresent() ? wallet.get() : null;
    }

    public Wallet getUserWallet(String id) {
        Optional<Wallet> wallet = walletRepository.findByUserId(id);
        return wallet.isPresent() ? wallet.get() : null;
    }

    public List<Wallet> getAll() {
        return walletRepository.findAll();
    }

    public Wallet update(Wallet wallet) {
        return walletRepository.save(wallet);
    }
}
