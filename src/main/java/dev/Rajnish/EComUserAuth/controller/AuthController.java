package dev.Rajnish.EComUserAuth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.Rajnish.EComUserAuth.dto.LoginRequestDTO;
import dev.Rajnish.EComUserAuth.dto.LogoutRequestDTO;
import dev.Rajnish.EComUserAuth.dto.SignUpRequestDTO;
import dev.Rajnish.EComUserAuth.dto.UserResponseDTO;
import dev.Rajnish.EComUserAuth.service.interfaces.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO)
    {
        boolean op = authService.signUp(signUpRequestDTO);
        if(op==false)
        {
            return new ResponseEntity<>("Unable to create account",HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>("Account created successfully",HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO)
    {
        return authService.login(loginRequestDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequestDTO logoutRequestDTO)
    {
        authService.logout(logoutRequestDTO.getUserId(), logoutRequestDTO.getToken());
        return new ResponseEntity<>("Logged out successfully",HttpStatus.OK);
    }    
}
