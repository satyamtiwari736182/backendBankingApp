package com.banking.app.bankingapp.services;

import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.banking.app.bankingapp.domain.UserRepositoryService;
import com.banking.app.bankingapp.domain.entity.User;
import com.banking.app.bankingapp.error.exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepositoryService userRepositoryService;
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	User u1 = User.builder().id("1").name("Ram").email("ram@ram").password("ram").phone(91123).roles("ROLE_USER")
			.build();

	@Test
	public void testAddUser_Success() {
		Mockito.when(userRepositoryService.getUserByName(anyString())).thenReturn(null);
		Mockito.when(userRepositoryService.getUserByEmail(anyString())).thenReturn(null);
		Mockito.when(userRepositoryService.add(u1)).thenReturn(u1);
		Mockito.when(passwordEncoder.encode(anyString())).thenReturn("jshfdf");
		User user = userService.addUser(u1);
		Assert.assertEquals("Ram", user.getName());
	}

	@Test
	public void testAddUser_Failure() {
		Mockito.when(userRepositoryService.getUserByName(anyString())).thenReturn(u1);
		Mockito.lenient().when(userRepositoryService.getUserByEmail(anyString())).thenReturn(null);
		UserNotFoundException thrown = Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.addUser(u1);
		});
		Assertions.assertEquals("Invalid username or email!!", thrown.getMessage());
	}

	@Test
	public void testGetUser_Success() {
		Mockito.when(userRepositoryService.get("1")).thenReturn(u1);
		User user = userService.getUser("1");
		Assert.assertEquals("Ram", user.getName());
	}

	@Test
	public void testGetUser_Failure() {
		Mockito.when(userRepositoryService.get("1")).thenReturn(null);
		UserNotFoundException thrown = Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.getUser("1");
		});
		Assertions.assertEquals("Invalid user with userId:1", thrown.getMessage());
	}

	@Test
	public void testGetUserByName_Success() {
		Mockito.when(userRepositoryService.getUserByName("Ram")).thenReturn(u1);
		User user = userService.getUserByName("Ram");
		Assert.assertEquals("Ram", user.getName());
	}

	@Test
	public void testGetUserByName_Failure() {
		Mockito.when(userRepositoryService.getUserByName(anyString())).thenReturn(null);
		UserNotFoundException thrown = Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.getUserByName("Ram");
		});
		Assertions.assertEquals("Invalid user with username:Ram", thrown.getMessage());
	}

	@Test
	public void testGetAllUser() {
		Mockito.when(userRepositoryService.getAll()).thenReturn(List.of(u1));
		List<User> user = userService.getAllUser();
		Assert.assertEquals(1, user.size());
	}
}
