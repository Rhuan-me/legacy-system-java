package com.group4.legacy_system_java.Mapper;

import com.group4.legacy_system_java.DTO.UserResponseDTO;
import com.group4.legacy_system_java.Model.User;

public class UserToUserResponseDtoMapper {

    public UserResponseDTO map(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDTO(user.getId(), user.getNome(), user.getLogin());
    }
}
