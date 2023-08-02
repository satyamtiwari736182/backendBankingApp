package com.banking.app.bankingapp;

import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.banking.app.bankingapp.error.ErrorResponse;
import com.banking.app.bankingapp.error.GlobalExceptionHandler;
import com.banking.app.bankingapp.error.exception.DuplicateWalletException;
import com.banking.app.bankingapp.error.exception.InsufficientBalanceException;
import com.banking.app.bankingapp.error.exception.UserNotFoundException;
import com.banking.app.bankingapp.error.exception.WalletNotFoundException;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTests {
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    public void testExceptionHandler() {
        BadCredentialsException exception = new BadCredentialsException("testing exception");
        ResponseEntity<ErrorResponse> res = globalExceptionHandler.exceptionHandler(exception);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    public void testHandleUserNotFoundException() {
        UserNotFoundException userNotFoundException = new UserNotFoundException("testing exception");
        ResponseEntity<ErrorResponse> res = globalExceptionHandler.handleUserNotFoundException(userNotFoundException);
        Assert.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    public void testHandleWalletNotFoundException() {

        WalletNotFoundException exception = new WalletNotFoundException("testing exception");
        ResponseEntity<ErrorResponse> res = globalExceptionHandler.handleWalletNotFoundException(exception);
        Assert.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    public void testHandleInsufficientBalanceException() {
        InsufficientBalanceException exception = new InsufficientBalanceException("testing exception");
        ResponseEntity<ErrorResponse> res = globalExceptionHandler.handleInsufficientBalanceException(exception);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    public void testHandleDuplicateWalletException() {
        DuplicateWalletException exception = new DuplicateWalletException("testing exception");
        ResponseEntity<ErrorResponse> res = globalExceptionHandler.handleDuplicateWalletException(exception);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

}
