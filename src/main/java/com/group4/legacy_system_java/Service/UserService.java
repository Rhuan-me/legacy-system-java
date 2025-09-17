package com.group4.legacy_system_java.Service;

import com.group4.legacy_system_java.DTO.UserUpdateDTO;
import com.group4.legacy_system_java.Exception.BadRequestException;
import com.group4.legacy_system_java.Exception.NotFoundException;
import com.group4.legacy_system_java.Model.User;
import com.group4.legacy_system_java.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll(){
        return this.userRepository.findAll();
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User findUserById(String id){
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
    }

    public User updateUser(String id, UserUpdateDTO updatedUser){
        if (updatedUser == null) {
            throw new BadRequestException("The updated user data cannot be null.");
        }
        User user = findUserById(id);
        user.setNome(updatedUser.nome());
        user.setLogin(updatedUser.login());
        user.setPassword(updatedUser.password());
        userRepository.save(user);
        return user;
    }

    public void deleteUser(String id){
        User user = findUserById(id);
        userRepository.delete(user);
    }




}
