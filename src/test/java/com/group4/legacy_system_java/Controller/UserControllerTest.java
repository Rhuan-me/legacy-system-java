package com.group4.legacy_system_java.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.legacy_system_java.DTO.UserCreateDTO;
import com.group4.legacy_system_java.DTO.UserResponseDTO;
import com.group4.legacy_system_java.DTO.UserUpdateDTO;
import com.group4.legacy_system_java.Exception.GlobalExceptionHandler;
import com.group4.legacy_system_java.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void findAll_ShouldReturnListOfUsers() throws Exception {
        UserResponseDTO userResponse = new UserResponseDTO("1", "Test User", "test@example.com");
        when(userService.findAll()).thenReturn(Collections.singletonList(userResponse));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Test User"));
    }

    @Test
    void findById_ShouldReturnUser() throws Exception {
        UserResponseDTO userResponse = new UserResponseDTO("1", "Test User", "test@example.com");
        when(userService.findUserById("1")).thenReturn(userResponse);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nome").value("Test User"));
    }

    @Test
    void create_ShouldReturnCreatedUser() throws Exception {
        UserCreateDTO createDTO = new UserCreateDTO("New User", "new@example.com", "ValidPass1@");
        UserResponseDTO responseDTO = new UserResponseDTO("1", "New User", "new@example.com");

        when(userService.createUser(any(UserCreateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/users/1"))
                .andExpect(jsonPath("$.nome").value("New User"));
    }

    @Test
    void update_ShouldReturnUpdatedUser() throws Exception {
        UserUpdateDTO updateDTO = new UserUpdateDTO("Updated User", "updated@example.com", "ValidPass1@");
        UserResponseDTO responseDTO = new UserResponseDTO("1", "Updated User", "updated@example.com");

        when(userService.updateUser(eq("1"), any(UserUpdateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Updated User"));
    }



    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
}
