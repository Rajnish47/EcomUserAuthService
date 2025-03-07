package dev.Rajnish.EComUserAuth.service.interfaces;

import java.util.List;
import java.util.UUID;

import dev.Rajnish.EComUserAuth.dto.LoginRequestDTO;
import dev.Rajnish.EComUserAuth.dto.SignUpRequestDTO;
import dev.Rajnish.EComUserAuth.dto.UserResponseDTO;
import dev.Rajnish.EComUserAuth.exceptions.user_exceptios.UserNotFoundException;

public interface UserService {

    UserResponseDTO getUserDetails(UUID userId) throws UserNotFoundException;
    Boolean signUp(SignUpRequestDTO signUpRequestDTO);
    UserResponseDTO login(LoginRequestDTO loginRequestDTO);
    UserResponseDTO setUserRoles(UUID userId, List<UUID> roleIds);    
}
