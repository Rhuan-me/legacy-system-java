package com.group4.legacy_system_java.Controller;

import com.group4.legacy_system_java.DTO.UserUpdateDTO;
import com.group4.legacy_system_java.Model.User;
import com.group4.legacy_system_java.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String error, String message, HttpStatus status){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);

        return ResponseEntity.status(status).body(body);
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id){
        try {
            return ResponseEntity.ok(userService.findUserById(id));
        } catch (RuntimeException e){
            return buildErrorResponse("User not found", e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<User> add(@RequestBody User user){
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody UserUpdateDTO user
    ){
        try{
            return ResponseEntity.ok(userService.updateUser(id, user));
        } catch (RuntimeException e){
            return buildErrorResponse("User not found", e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e){
            return buildErrorResponse("Categoria não encontrada", e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
