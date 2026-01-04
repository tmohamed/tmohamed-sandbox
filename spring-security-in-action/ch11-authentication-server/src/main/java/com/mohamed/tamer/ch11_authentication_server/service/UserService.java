package com.mohamed.tamer.ch11_authentication_server.service;

import com.mohamed.tamer.ch11_authentication_server.model.entity.Otp;
import com.mohamed.tamer.ch11_authentication_server.model.entity.User;
import com.mohamed.tamer.ch11_authentication_server.model.repository.OtpRepository;
import com.mohamed.tamer.ch11_authentication_server.model.repository.UserRepository;
import com.mohamed.tamer.ch11_authentication_server.util.GenerateCodeUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;

    public void addUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void auth(User user){
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if(optionalUser.isPresent()){
            User persistedUser = optionalUser.get();
            if(passwordEncoder.matches(user.getPassword(), persistedUser.getPassword())){
                renewOtp(persistedUser);
            } else{
                throw new BadCredentialsException("Bad Credentials");
            }
        }
    }

    private void renewOtp(User user){
        String code = GenerateCodeUtil.generateCode();
        Optional<Otp> optionalOtp = otpRepository.findByUsername(user.getUsername());

        if(optionalOtp.isPresent()){
            Otp otp = optionalOtp.get();
            otp.setCode(code);

        } else{
            Otp otp = new Otp();
            otp.setUsername(user.getUsername());
            otp.setCode(code);
            otpRepository.save(otp);
        }
    }

    public boolean check(Otp otpToValidate){
        Optional<Otp> userOtp = otpRepository.findByUsername(otpToValidate.getUsername());
        if(userOtp.isPresent()){
            Otp persistedOtp = userOtp.get();
            if(otpToValidate.getCode().equals(persistedOtp.getCode())) {
                return true;
            }
        }

        return false;
    }
}
