package com.banking.app.bankingapp.domain.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.app.bankingapp.domain.entity.User;
import com.banking.app.bankingapp.dto.UserDto;

@Service
public class UserDtoMapper {
    @Autowired
    private ModelMapper modelMapper;

    public User dtoToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public UserDto userToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
