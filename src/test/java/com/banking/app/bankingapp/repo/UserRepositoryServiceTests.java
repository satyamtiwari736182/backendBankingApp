package com.banking.app.bankingapp.repo;

import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banking.app.bankingapp.domain.UserRepositoryService;
import com.banking.app.bankingapp.domain.entity.User;
import com.banking.app.bankingapp.domain.repo.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryServiceTests {
    @InjectMocks
    private UserRepositoryService userRepositoryService;
    @Mock
    private UserRepository userRepository;

    User u1 = User.builder().id("1").name("Ram").email("ram@ram").password("ram").phone(91123).roles("ROLE_USER")
            .build();

    @Test
    public void testAdd() {
        Mockito.when(userRepository.insert(u1)).thenReturn(u1);
        User user = userRepositoryService.add(u1);
        Assert.assertEquals(u1, user);
    }

    @Test
    public void testGet() {
        Mockito.when(userRepository.findById(anyString())).thenReturn(Optional.of(u1));
        User user = userRepositoryService.get(anyString());
        Assert.assertEquals(u1, user);
    }

    @Test
    public void testGetAll() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(u1));
        List<User> users = userRepositoryService.getAll();
        Assert.assertEquals(1, users.size());
    }

    @Test
    public void testGetUserByName() {
        Mockito.when(userRepository.findByName(anyString())).thenReturn(Optional.of(u1));
        User user = userRepositoryService.getUserByName(anyString());
        Assert.assertEquals(u1, user);
    }

    @Test
    public void getUserByEmail() {
        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(u1));
        User user = userRepositoryService.getUserByEmail(anyString());
        Assert.assertEquals(u1, user);
    }
}
