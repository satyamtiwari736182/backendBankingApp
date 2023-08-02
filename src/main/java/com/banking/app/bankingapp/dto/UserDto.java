package com.banking.app.bankingapp.dto;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class UserDto {
    @NotEmpty
    private String id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String email;
    @NotNull
    private int phone;
}
