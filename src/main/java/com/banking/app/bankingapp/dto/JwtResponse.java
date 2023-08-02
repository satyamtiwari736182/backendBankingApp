package com.banking.app.bankingapp.dto;

import org.springframework.stereotype.Component;

import com.banking.app.bankingapp.domain.entity.User;
import com.banking.app.bankingapp.domain.entity.Wallet;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class JwtResponse {
    @NotEmpty
    private String jwtToken;
    @NotEmpty
    private String userId;
    @NotEmpty
    private String walletId;
}
