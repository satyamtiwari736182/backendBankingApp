package com.banking.app.bankingapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.app.bankingapp.domain.entity.Transaction;
import com.banking.app.bankingapp.domain.entity.User;
import com.banking.app.bankingapp.domain.entity.Wallet;
import com.banking.app.bankingapp.domain.enums.TransactionType;
import com.banking.app.bankingapp.domain.mapper.UserDtoMapper;
import com.banking.app.bankingapp.dto.TransferDto;
import com.banking.app.bankingapp.dto.UserDto;
import com.banking.app.bankingapp.error.exception.UserNotFoundException;
import com.banking.app.bankingapp.services.TransactionService;
import com.banking.app.bankingapp.services.UserService;
import com.banking.app.bankingapp.services.WalletService;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/user")
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserDtoMapper userMapper;



    @PostMapping("/signup")
    public ResponseEntity<UserDto> add(@Valid @RequestBody User user) {
        user = userService.addUser(user);
        return ResponseEntity.created(null).body(userMapper.userToDto(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> get() {
        List<User> users = userService.getAllUser();
        return ResponseEntity.ok().body(users.stream().map(user -> userMapper.userToDto(user)).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@Valid @PathVariable String id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok().body(userMapper.userToDto(user));
    }

    // !====================================================================
    @PostMapping("/wallet")
    public ResponseEntity<Wallet> createWallet(@Valid @RequestBody Wallet wallet) {
        if (userService.getUser(wallet.getUserId()) == null)
            throw new UserNotFoundException("Invalid user with userId:" +
                    wallet.getUserId());
        wallet = walletService.createWallet(wallet);
        return ResponseEntity.created(null).body(wallet);

    }

    @GetMapping("/wallet/{id}")
    public Wallet getWallet(@PathVariable String id) {
        return walletService.getWallet(id);
    }

    @GetMapping("/wallet")
    public List<Wallet> getAllWallet() {
        List<Wallet> wallets = walletService.getAllWallet();
        return wallets;
    }

    @PutMapping("/wallet/transaction")
    public ResponseEntity<Transaction> transferWallet(@Valid @RequestBody Transaction transaction) {
        transaction = walletService.transferWallet(transaction);
        return ResponseEntity.ok().body(transaction);
    }

    @PostMapping("/wallet/transaction")
    public ResponseEntity<Transaction> rechargeWallet(@Valid @RequestBody Transaction transaction) {
        transaction = walletService.rechageWallet(transaction);
        return ResponseEntity.created(null).body(transaction);
    }

    @PostMapping("/wallet/transaction/transfer")
    public ResponseEntity<Transaction> transfer(@Valid @RequestBody TransferDto transaction) {

        Transaction transactionData = walletService.transfer(transaction);
        return ResponseEntity.ok().body(transactionData);

    }

    @GetMapping("/transaction")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransaction();
    }

    @GetMapping("/transaction/{id}")
    public List<Transaction> getTransactions(@PathVariable String id) {
        return transactionService.getAllUserTransaction(id);
    }

    @GetMapping("/reward/{id}")
    public List<Transaction> getRewards(@PathVariable String id) {
        return transactionService.getRewards(id, TransactionType.RECHARGE);
    }

}
