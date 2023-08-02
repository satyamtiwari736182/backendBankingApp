package com.banking.app.bankingapp.dto;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
public class JwtRequest {
    @NotEmpty
    @Size(min = 3)
    private String username;
    @NotEmpty
    @Size(min = 3)
    private String password;
}
