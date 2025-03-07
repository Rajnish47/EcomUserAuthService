package dev.Rajnish.EComUserAuth.service.interfaces;

import java.util.List;
import java.util.UUID;

import dev.Rajnish.EComUserAuth.dto.UserResponseDTO;
import dev.Rajnish.EComUserAuth.exceptions.user_exceptios.UserNotFoundException;

public interface UserService {

    UserResponseDTO getUserDetails(UUID userId) throws UserNotFoundException;
    UserResponseDTO setUserRoles(UUID userId, List<UUID> roleIds);    
}
