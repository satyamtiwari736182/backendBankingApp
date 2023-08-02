package com.banking.app.bankingapp;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.banking.app.bankingapp.controller.HomeController;
import com.banking.app.bankingapp.domain.entity.Transaction;
import com.banking.app.bankingapp.domain.entity.User;
import com.banking.app.bankingapp.domain.entity.Wallet;
import com.banking.app.bankingapp.domain.enums.TransactionType;
import com.banking.app.bankingapp.domain.mapper.UserDtoMapper;
import com.banking.app.bankingapp.dto.TransferDto;
import com.banking.app.bankingapp.dto.UserDto;
import com.banking.app.bankingapp.services.TransactionService;
import com.banking.app.bankingapp.services.UserService;
import com.banking.app.bankingapp.services.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import jakarta.servlet.ServletException;

@ExtendWith(MockitoExtension.class)
public class HomeControllerTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	ModelMapper modelMapper;
	ObjectMapper objectMapper = new ObjectMapper();
	ObjectWriter objectWriter = objectMapper.writer();

	@InjectMocks
	private HomeController homeController;
	@Mock
	private UserService userService;
	@Mock
	private WalletService walletService;
	@Mock
	private TransactionService transactionService;
	@Mock
	private UserDtoMapper userMapper;

	User u1 = User.builder().id("1").name("Ram").email("ram@ram").password("ram").phone(91123).roles("ROLE_USER")
			.build();
	UserDto u2 = UserDto.builder().id("1").name("Ram").email("ram@ram").phone(91123).build();
	Wallet w1 = Wallet.builder().userId("1").walletId("123ram").build();
	Transaction t1 = Transaction.builder().id("123").amount(10).category(TransactionType.RECHARGE).walletId("123ram")
			.dateTime(LocalDateTime.now())
			.build();

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
	}


	@Test
	public void testSignup() throws Exception {
		Mockito.lenient().when(userService.addUser(u1)).thenReturn(u1);
		Mockito.lenient().when(userMapper.userToDto(any())).thenReturn(u2);
		String content = objectWriter.writeValueAsString(u1);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/user/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content);

		mockMvc.perform(mockRequest)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	public void testGetAllUser() throws Exception {
		List<User> userList = List.of(u1);
		Mockito.when(userService.getAllUser()).thenReturn(userList);
		Mockito.when(userMapper.userToDto(u1)).thenReturn(u2);
		this.mockMvc.perform(get("/api/user")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name", is("Ram")))
				.andReturn();
	}

	@Test
	public void testGetUser() throws Exception {
		Mockito.when(userService.getUser("1")).thenReturn(u1);
		Mockito.when(userMapper.userToDto(u1)).thenReturn(u2);
		this.mockMvc.perform(get("/api/user/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	public void testCreateWallet_Success() throws Exception {
		Mockito.lenient().when(walletService.createWallet(w1)).thenReturn(w1);
		Mockito.lenient().when(userService.getUser(anyString())).thenReturn(u1);

		String content = objectWriter.writeValueAsString(w1);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/user/wallet")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content);

		mockMvc.perform(mockRequest)
				.andExpect(status().isCreated());
	}

	@Test
	public void testCreateWallet_Failure() throws Exception {
		// Mockito.when(walletService.createWallet(w1)).thenReturn(w1);
		Mockito.when(userService.getUser(anyString())).thenReturn(null);
		String content = objectWriter.writeValueAsString(w1);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/user/wallet")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content);
		ServletException thrown = Assert.assertThrows(ServletException.class, () -> {
			mockMvc.perform(mockRequest);
		});
		Assert.assertEquals(
				"Request processing failed: com.banking.app.bankingapp.error.exception.UserNotFoundException: Invalid user with userId:1",
				thrown.getMessage());
	}

	@Test
	public void testGetWallet() throws Exception {
		Mockito.when(walletService.getWallet(anyString())).thenReturn(w1);
		this.mockMvc.perform(get("/api/user/wallet/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	public void testGetAllWallet() throws Exception {
		Mockito.when(walletService.getAllWallet()).thenReturn(List.of(w1));
		this.mockMvc.perform(get("/api/user/wallet")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$[0].userId", is("1")));
	}

	@Test
	public void testRechargeWallet() throws Exception {
		Mockito.lenient().when(walletService.rechageWallet(t1)).thenReturn(t1);
		ResponseEntity<Transaction> res = homeController.rechargeWallet(t1);
		Assert.assertEquals(t1, res.getBody());

	}

	@Test
	public void testWallet() throws Exception {
		TransferDto td1 = TransferDto.builder().senderWalletId("1").receiverWalletId("2").amount(10).build();
		Mockito.when(walletService.transfer(td1)).thenReturn(t1);
		ResponseEntity<Transaction> res = homeController.transfer(td1);
		Assert.assertEquals(t1, res.getBody());

	}

	@Test
	public void testTransferWallet() throws Exception {
		Mockito.lenient().when(walletService.transferWallet(t1)).thenReturn(t1);
		ResponseEntity<Transaction> res = homeController.transferWallet(t1);
		Assert.assertEquals(t1, res.getBody());

	}

	@Test
	public void testGetAllTransaction() throws Exception {
		Mockito.lenient().when(transactionService.getAllTransaction()).thenReturn(List.of(t1));
		this.mockMvc.perform(get("/api/user/transaction")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetTransaction() throws Exception {
		Mockito.lenient().when(transactionService.getAllUserTransaction("123ram")).thenReturn(List.of(t1));
		this.mockMvc.perform(get("/api/user/transaction/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetReward() throws Exception {
		Mockito.lenient().when(transactionService.getRewards("123ram", TransactionType.RECHARGE))
				.thenReturn(List.of(t1));
		this.mockMvc.perform(get("/api/user/reward/123ram")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

	}
}
