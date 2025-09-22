package com.group4.legacy_system_java.Mapper;

import com.group4.legacy_system_java.DTO.UserUpdateDTO;
import com.group4.legacy_system_java.Model.User;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateDtoToUserMapper {
    public User map(UserUpdateDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setNome(dto.nome());
        user.setLogin(dto.login());
        user.setPassword(dto.password());

        return user;
    }
}
