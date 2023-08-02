package com.banking.app.bankingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.app.bankingapp.domain.WalletRepositoryService;
import com.banking.app.bankingapp.domain.entity.Wallet;
import com.banking.app.bankingapp.domain.mapper.UserDtoMapper;
import com.banking.app.bankingapp.dto.JwtRequest;
import com.banking.app.bankingapp.dto.JwtResponse;
import com.banking.app.bankingapp.dto.UserDto;
import com.banking.app.bankingapp.services.UserService;
import com.banking.app.bankingapp.utils.JwtHelper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/user")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private WalletRepositoryService walletRepositoryService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;
    @Autowired
    private UserDtoMapper userDtoMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody JwtRequest request, HttpServletRequest req) {
        String username = request.getUsername(), password = request.getPassword();
        this.doAuthenticate(username, password);
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.jwtHelper.generateToken(userDetails);
        UserDto user = userDtoMapper.userToDto(userService.getUserByName(username));
        Wallet wallet = walletRepositoryService.getUserWallet(user.getId());
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .userId(user.getId())
                .walletId(wallet != null ? wallet.getWalletId() : null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
                password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

}

// @PostMapping("/logout")
// public String logout(HttpServletRequest request) {
// String requestHeader = request.getHeader("Authorization");
// String username = null, token = null;
// if (username == null && requestHeader.startsWith("Bearer")) {
// token = requestHeader.substring(7);
// username = jwtHelper.getUsernameFromToken(token);
// }
// UserDetails userDetails = userDetailsService.loadUserByUsername(username);
// if (jwtHelper.validateToken(token, userDetails)) {
// UsernamePasswordAuthenticationToken authentication = new
// UsernamePasswordAuthenticationToken(
// userDetails.getUsername(), userDetails.getPassword());
// authentication.eraseCredentials();
// authentication.setAuthenticated(false);
// authentication.setDetails(null);
// SecurityContextHolder.getContext().setAuthentication(null);
// } else
// return "invalid user @ " + username;

// return "logged out successfully....";

// }