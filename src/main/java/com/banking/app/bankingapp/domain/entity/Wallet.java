package com.banking.app.bankingapp.domain.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import com.banking.app.bankingapp.domain.enums.WalletType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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
public class Wallet {
    @Id
    @NotEmpty
    private String userId;
    @NotEmpty
    private String walletId;
    @Builder.Default
    private WalletType category = WalletType.PERSONAL;
    @Builder.Default
    private double balance = 0.0;
    @Builder.Default
    private double cashBack = 0.0;
    @Builder.Default
    private List<String> transaction = new ArrayList<>();
}
