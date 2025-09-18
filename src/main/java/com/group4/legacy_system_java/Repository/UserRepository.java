package com.group4.legacy_system_java.Repository;

import com.group4.legacy_system_java.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Importe Optional

public interface UserRepository extends JpaRepository<User, String> {
    // Este método irá buscar um usuário pelo campo 'login'
    Optional<User> findByLogin(String login);
}