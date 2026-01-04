package com.mohamed.tamer.ch11_authentication_server.model.repository;

import com.mohamed.tamer.ch11_authentication_server.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}
