package dev.Rajnish.EComUserAuth.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.Rajnish.EComUserAuth.dto.LoginRequestDTO;
import dev.Rajnish.EComUserAuth.dto.SignUpRequestDTO;
import dev.Rajnish.EComUserAuth.dto.UserResponseDTO;
import dev.Rajnish.EComUserAuth.entity.Role;
import dev.Rajnish.EComUserAuth.entity.User;
import dev.Rajnish.EComUserAuth.exceptions.user_exceptios.UserNotFoundException;
import dev.Rajnish.EComUserAuth.repository.RoleRepository;
import dev.Rajnish.EComUserAuth.repository.UserRepository;
import dev.Rajnish.EComUserAuth.service.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserResponseDTO getUserDetails(UUID userId) throws UserNotFoundException {

        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty())
        {
            throw new UserNotFoundException("User not found");
        }
        
        User savedUser = userOptional.get();
        
        return UserResponseDTO.createUserResponseDTO(savedUser);
    }

    @Override
    public Boolean signUp(SignUpRequestDTO signUpRequestDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'signUp'");
    }

    @Override
    public UserResponseDTO login(LoginRequestDTO loginRequestDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    @Override
    public UserResponseDTO setUserRoles(UUID userId, List<UUID> roleIds)throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty())
        {
            throw new UserNotFoundException("User not found");
        }
        
        User savedUser = userOptional.get();
        Set<Role> savedRoles = roleRepository.findAllByIdIn(roleIds);
        savedUser.setUserRoles(savedRoles);
        userRepository.save(savedUser);

        return UserResponseDTO.createUserResponseDTO(savedUser);
    }
    
}
