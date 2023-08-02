package com.banking.app.bankingapp.dto;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.meanbean.test.BeanTester;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banking.app.bankingapp.error.ErrorResponse;

@ExtendWith(MockitoExtension.class)
public class DTOtesterTests {
    @Test
    public void testAllDTOs() {
        BeanTester beanTester = new BeanTester();
        beanTester.testBean(UserDto.class);
        beanTester.testBean(JwtRequest.class);
        beanTester.testBean(JwtResponse.class);
        beanTester.testBean(TransferDto.class);

        // beanTester.testBean(ErrorResponse.class);
    }

    @InjectMocks
    private ErrorResponse errorResponse;

    @Test
    public void testError() {
        LocalDateTime time = LocalDateTime.now();
        errorResponse.setDateTime(time);
        errorResponse.setMsg("test error");
        errorResponse.setStatus(200);
        Assert.assertTrue(time.isBefore(errorResponse.getDateTime()) || time.isEqual(errorResponse.getDateTime()));
        Assert.assertEquals("test error", errorResponse.getMsg());
        Assert.assertEquals(200, errorResponse.getStatus());
    }
}
