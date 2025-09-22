package com.group4.legacy_system_java.Service;

import com.group4.legacy_system_java.DTO.UserCreateDTO;
import com.group4.legacy_system_java.DTO.UserResponseDTO;
import com.group4.legacy_system_java.DTO.UserUpdateDTO;
import com.group4.legacy_system_java.Exception.BadRequestException;
import com.group4.legacy_system_java.Exception.NotFoundException;
import com.group4.legacy_system_java.Model.User;
import com.group4.legacy_system_java.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("1", "Test User", "test@example.com", "encodedPassword");
    }

    @Test
    void findAll_ShouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        List<UserResponseDTO> result = userService.findAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test User", result.get(0).nome());
    }

    @Test
    void findUserById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        UserResponseDTO result = userService.findUserById("1");
        assertNotNull(result);
        assertEquals("Test User", result.nome());
    }

    @Test
    void findUserById_WhenUserDoesNotExist_ShouldThrowNotFoundException() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findUserById("1"));
    }

    @Test
    void createUser_WhenLoginIsUnique_ShouldCreateUser() {
        UserCreateDTO createDTO = new UserCreateDTO("New User", "new@example.com", "password123");
        when(userRepository.findByLogin(createDTO.login())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(createDTO.password())).thenReturn("encodedPassword");

        User savedUser = new User("2", createDTO.nome(), createDTO.login(), "encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO result = userService.createUser(createDTO);

        assertNotNull(result);
        assertEquals("New User", result.nome());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_WhenLoginAlreadyExists_ShouldThrowBadRequestException() {
        UserCreateDTO createDTO = new UserCreateDTO("New User", "test@example.com", "password123");
        when(userRepository.findByLogin(createDTO.login())).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> userService.createUser(createDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateUser() {
        UserUpdateDTO updateDTO = new UserUpdateDTO("Updated Name", "updated@example.com", "anyPassword");
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = userService.updateUser("1", updateDTO);

        assertNotNull(result);
        assertEquals("Updated Name", user.getNome());
        assertEquals("updated@example.com", user.getLogin());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUser("1");

        verify(userRepository, times(1)).delete(user);
    }
}
