package com.group4.legacy_system_java.Repository;

import com.group4.legacy_system_java.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByLogin(String login);
}