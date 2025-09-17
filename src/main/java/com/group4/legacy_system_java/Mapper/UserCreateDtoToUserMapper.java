package com.group4.legacy_system_java.Mapper;

import com.group4.legacy_system_java.DTO.UserCreateDTO;
import com.group4.legacy_system_java.Exception.BadRequestException;
import com.group4.legacy_system_java.Model.User;

public class UserCreateDtoToUserMapper {
    public User map(UserCreateDTO userCreateDto) {
        if (userCreateDto == null) {
            throw new BadRequestException("The user data provided is null.");
        }
        User user = new User();
        user.setId(null);
        user.setNome(userCreateDto.nome());
        user.setLogin(userCreateDto.login());
        user.setPassword(userCreateDto.password());

        return user;
    }
}
