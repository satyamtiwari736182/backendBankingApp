package com.banking.app.bankingapp.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import com.banking.app.bankingapp.domain.enums.TransactionType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
@Document
public class Transaction {
    @Id
    private String id;
    private TransactionType category;
    @NotNull
    private String walletId;
    @Builder.Default
    private String tranferWalletId = "_";
    @Min(1)
    private double amount;
    @Builder.Default
    private double cashBack = 0.0;
    @Builder.Default
    private LocalDateTime dateTime = LocalDateTime.now();
}
