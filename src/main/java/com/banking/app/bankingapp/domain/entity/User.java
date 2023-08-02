package com.banking.app.bankingapp.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class User {
    @Id
    private String id;
    @NotEmpty
    @Size(min = 3)
    private String name;
    @Email
    private String email;
    @NotEmpty
    @Size(min = 3)
    private String password;
    @NotNull
    private int phone;
    @Builder.Default
    private String roles = "ROLE_USER";
}
