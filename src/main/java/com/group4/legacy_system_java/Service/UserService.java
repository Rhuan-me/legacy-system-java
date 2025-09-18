package com.group4.legacy_system_java.Service;

import com.group4.legacy_system_java.DTO.UserCreateDTO;
import com.group4.legacy_system_java.DTO.UserResponseDTO;
import com.group4.legacy_system_java.DTO.UserUpdateDTO;
import com.group4.legacy_system_java.Exception.BadRequestException;
import com.group4.legacy_system_java.Exception.NotFoundException;
import com.group4.legacy_system_java.Model.User;
import com.group4.legacy_system_java.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService (UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponseDTO> findAll(){
        return this.userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getNome(), user.getLogin()))
                .collect(Collectors.toList());
    }

    public UserResponseDTO findUserById(String id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        return new UserResponseDTO(user.getId(), user.getNome(), user.getLogin());
    }

    public UserResponseDTO createUser(UserCreateDTO userCreateDTO){
        // Verifica se o email já existe
        userRepository.findByLogin(userCreateDTO.login()).ifPresent(user -> {
            logger.error("Validation failure: Login '{}' already exists.", userCreateDTO.login());
            throw new BadRequestException("The provided login is already in use.");
        });

        User user = new User();
        user.setNome(userCreateDTO.nome());
        user.setLogin(userCreateDTO.login());
        // Criptografa a senha antes de salvar
        user.setPassword(passwordEncoder.encode(userCreateDTO.password()));

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(savedUser.getId(), savedUser.getNome(), savedUser.getLogin());
    }

    public UserResponseDTO updateUser(String id, UserUpdateDTO updatedUserDTO){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));

        user.setNome(updatedUserDTO.nome());
        user.setLogin(updatedUserDTO.login());
        // A senha não deve ser alterada por este endpoint

        User updatedUser = userRepository.save(user);
        return new UserResponseDTO(updatedUser.getId(), updatedUser.getNome(), updatedUser.getLogin());
    }

    public void deleteUser(String id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        userRepository.delete(user);
    }
}