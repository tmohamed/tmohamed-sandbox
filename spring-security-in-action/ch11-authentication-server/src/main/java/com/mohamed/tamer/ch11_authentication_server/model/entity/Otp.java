package com.mohamed.tamer.ch11_authentication_server.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "otps")
public class Otp {
    @Id
    private String username;

    private String code;
}
