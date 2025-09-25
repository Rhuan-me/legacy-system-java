package com.group4.legacy_system_java.Mapper;

import com.group4.legacy_system_java.DTO.UserCreateDTO;
import com.group4.legacy_system_java.DTO.UserResponseDTO;
import com.group4.legacy_system_java.DTO.UserUpdateDTO;
import com.group4.legacy_system_java.Exception.BadRequestException;
import com.group4.legacy_system_java.Model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserMappersTest {

    private final UserCreateDtoToUserMapper userCreateDtoToUserMapper = new UserCreateDtoToUserMapper();
    private final UserUpdateDtoToUserMapper userUpdateDtoToUserMapper = new UserUpdateDtoToUserMapper();
    private final UserToUserResponseDtoMapper userToUserResponseDtoMapper = new UserToUserResponseDtoMapper();

    // Testes para UserCreateDtoToUserMapper
    @Test
    void map_UserCreateDTO_ToUser_ShouldMapCorrectly() {
        UserCreateDTO dto = new UserCreateDTO("Test User", "test@example.com", "password123");
        User user = userCreateDtoToUserMapper.map(dto);

        assertNotNull(user);
        assertNull(user.getId());
        assertEquals(dto.nome(), user.getNome());
        assertEquals(dto.login(), user.getLogin());
        assertEquals(dto.password(), user.getPassword());
    }

    @Test
    void map_UserCreateDTO_WhenNull_ShouldThrowBadRequestException() {
        assertThrows(BadRequestException.class, () -> userCreateDtoToUserMapper.map(null));
    }

    // Testes para UserUpdateDtoToUserMapper
    @Test
    void map_UserUpdateDTO_ToUser_ShouldMapCorrectly() {
        UserUpdateDTO dto = new UserUpdateDTO("Updated User", "updated@example.com", "newPassword456");
        User user = userUpdateDtoToUserMapper.map(dto);

        assertNotNull(user);
        assertEquals(dto.nome(), user.getNome());
        assertEquals(dto.login(), user.getLogin());
        assertEquals(dto.password(), user.getPassword());
    }

    @Test
    void map_UserUpdateDTO_WhenNull_ShouldReturnNull() {
        assertNull(userUpdateDtoToUserMapper.map(null));
    }

    // Testes para UserToUserResponseDtoMapper
    @Test
    void map_User_ToUserResponseDTO_ShouldMapCorrectly() {
        User user = new User("1", "Test User", "test@example.com", "encodedPassword");
        UserResponseDTO dto = userToUserResponseDtoMapper.map(user);

        assertNotNull(dto);
        assertEquals(user.getId(), dto.id());
        assertEquals(user.getNome(), dto.nome());
        assertEquals(user.getLogin(), dto.login());
    }

    @Test
    void map_User_WhenNull_ShouldReturnNull() {
        assertNull(userToUserResponseDtoMapper.map(null));
    }
}