package dev.Rajnish.EComUserAuth.service.interfaces;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import dev.Rajnish.EComUserAuth.dto.LoginRequestDTO;
import dev.Rajnish.EComUserAuth.dto.SignUpRequestDTO;
import dev.Rajnish.EComUserAuth.dto.UserResponseDTO;
import dev.Rajnish.EComUserAuth.dto.ValidateTokenRequestDTO;

public interface AuthService {

    Boolean adminSignUp(SignUpRequestDTO signUpRequestDTO);
    ResponseEntity<UserResponseDTO> login(LoginRequestDTO loginRequestDTO);
    Boolean signUp(SignUpRequestDTO signUpRequestDTO);
    Boolean logout(UUID userId, String token);
    boolean validate(String token,ValidateTokenRequestDTO validateTokenRequestDTO);  
}
