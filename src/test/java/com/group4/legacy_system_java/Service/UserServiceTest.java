package com.group4.legacy_system_java.Service;

import com.group4.legacy_system_java.DTO.UserCreateDTO;
import com.group4.legacy_system_java.DTO.UserResponseDTO;
import com.group4.legacy_system_java.Exception.BadRequestException;
import com.group4.legacy_system_java.Exception.NotFoundException;
import com.group4.legacy_system_java.Mapper.UserCreateDtoToUserMapper;
import com.group4.legacy_system_java.Mapper.UserToUserResponseDtoMapper;
import com.group4.legacy_system_java.Model.User;
import com.group4.legacy_system_java.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita a injeção de mocks do Mockito
public class UserServiceTest {

    // @Mock cria uma simulação (um "dublê") das dependências
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserCreateDtoToUserMapper userCreateDtoToUserMapper;

    @Mock
    private UserToUserResponseDtoMapper userToUserResponseDtoMapper;

    // @InjectMocks injeta os @Mocks acima na classe que estamos testando
    @InjectMocks
    private UserService userService;

    private User user;
    private UserCreateDTO userCreateDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        // Configuração inicial para os testes
        userCreateDTO = new UserCreateDTO("Test User", "test@user.com", "password123");

        user = new User();
        user.setId("test-id-123");
        user.setNome("Test User");
        user.setLogin("test@user.com");
        user.setPassword("hashed_password"); // Senha já "hasheada"

        userResponseDTO = new UserResponseDTO("test-id-123", "Test User", "test@user.com");
    }

    @Test
    void testCreateUser_Success() {
        // --- Arrange (Organizar) ---

        // 1. Simular a verificação de login (usuário NÃO existe)
        when(userRepository.findByLogin(userCreateDTO.login())).thenReturn(Optional.empty());

        // 2. Simular o mapper de DTO para Entidade
        when(userCreateDtoToUserMapper.map(userCreateDTO)).thenReturn(user);

        // 3. Simular o "hash" da senha
        when(passwordEncoder.encode(userCreateDTO.password())).thenReturn("hashed_password");

        // 4. Simular o salvamento no repositório (agora MongoDB)
        when(userRepository.save(any(User.class))).thenReturn(user);

        // 5. Simular o mapper de Entidade para DTO de Resposta
        when(userToUserResponseDtoMapper.map(user)).thenReturn(userResponseDTO);


        // --- Act (Agir) ---
        UserResponseDTO result = userService.createUser(userCreateDTO);


        // --- Assert (Verificar) ---
        assertNotNull(result);
        assertEquals("test-id-123", result.id());
        assertEquals("test@user.com", result.login());

        // Verificar se a senha foi hasheada antes de salvar
        verify(user).setPassword("hashed_password");

        // Verificar se os métodos corretos foram chamados
        verify(userRepository, times(1)).findByLogin("test@user.com");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUser_Failure_LoginAlreadyExists() {
        // --- Arrange ---
        // Simular que o usuário JÁ existe no banco
        when(userRepository.findByLogin(userCreateDTO.login())).thenReturn(Optional.of(user));

        // --- Act & Assert ---
        // Verificar se a exceção correta é lançada
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.createUser(userCreateDTO);
        });

        assertEquals("Login já cadastrado", exception.getMessage());

        // Garantir que o método save NUNCA foi chamado
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testFindUserById_Success() {
        // --- Arrange ---
        when(userRepository.findById("test-id-123")).thenReturn(Optional.of(user));
        when(userToUserResponseDtoMapper.map(user)).thenReturn(userResponseDTO);

        // --- Act ---
        UserResponseDTO result = userService.findUserById("test-id-123");

        // --- Assert ---
        assertNotNull(result);
        assertEquals(userResponseDTO, result);
        verify(userRepository, times(1)).findById("test-id-123");
    }

    @Test
    void testFindUserById_Failure_NotFound() {
        // --- Arrange ---
        when(userRepository.findById("wrong-id")).thenReturn(Optional.empty());

        // --- Act & Assert ---
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.findUserById("wrong-id");
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userRepository, times(1)).findById("wrong-id");
    }
}