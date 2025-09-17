package com.group4.legacy_system_java.Repository;

import com.group4.legacy_system_java.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
