package com.banking.app.bankingapp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.banking.app.bankingapp.config.UserInfoDetails;
import com.banking.app.bankingapp.controller.AuthController;
import com.banking.app.bankingapp.domain.WalletRepositoryService;
import com.banking.app.bankingapp.domain.entity.User;
import com.banking.app.bankingapp.domain.entity.Wallet;
import com.banking.app.bankingapp.domain.mapper.UserDtoMapper;
import com.banking.app.bankingapp.dto.JwtRequest;
import com.banking.app.bankingapp.dto.JwtResponse;
import com.banking.app.bankingapp.dto.UserDto;
import com.banking.app.bankingapp.services.UserService;
import com.banking.app.bankingapp.utils.JwtHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTests {
    @InjectMocks
    private AuthController authController;
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private UserService userService;

    @Mock
    private UserDtoMapper userDtoMapper;
    @Mock
    private WalletRepositoryService walletRepositoryService;
    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        authenticationManager = mock(AuthenticationManager.class);
    }

    @Test
    public void testLogin() throws Exception {
        User u1 = User.builder().id("1").name("Ram").email("ram@ram").password("ram").phone(91123).roles("ROLE_USER")
                .build();
        Wallet w1 = Wallet.builder().userId("1").walletId("123ram").build();

        UserDto uDto = UserDto.builder().id("1").name("Ram").email("ram@ram").phone(91123).build();
        UserDetails userDetails = new UserInfoDetails(u1);
        Mockito.when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        Mockito.when(jwtHelper.generateToken(userDetails)).thenReturn("randomtoken1324");
        Mockito.when(userService.getUserByName(anyString())).thenReturn(u1);
        Mockito.when(userDtoMapper.userToDto(any())).thenReturn(uDto);
        Mockito.when(walletRepositoryService.getUserWallet(any())).thenReturn(w1);

        JwtRequest request = JwtRequest.builder().username("Ram").password("ram").build();
        MvcResult mvcResult = mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        JwtResponse response = asObject(responseBody, JwtResponse.class);

    }

    // Utility methods to convert objects to/from JSON
    private static String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }

    private static <T> T asObject(String json, Class<T> clazz) throws Exception {
        return new ObjectMapper().readValue(json, clazz);
    }

}

// @Test
// public void testDoAuthenticate_ValidCredentials() {
// String username = "Ram";
// String password = "ram";
// Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
// .thenReturn(new UsernamePasswordAuthenticationToken(username, password));
// verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
// }