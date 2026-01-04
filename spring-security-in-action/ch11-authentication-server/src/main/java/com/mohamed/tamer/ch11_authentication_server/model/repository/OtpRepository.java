package com.mohamed.tamer.ch11_authentication_server.model.repository;

import com.mohamed.tamer.ch11_authentication_server.model.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, String> {
    Optional<Otp> findByUsername(String username);
}
