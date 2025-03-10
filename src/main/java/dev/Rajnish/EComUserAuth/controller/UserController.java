package dev.Rajnish.EComUserAuth.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.Rajnish.EComUserAuth.dto.SetUserRolesDTO;
import dev.Rajnish.EComUserAuth.dto.UserResponseDTO;
import dev.Rajnish.EComUserAuth.service.interfaces.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserDetails(@PathVariable("id") UUID userId)
    {
        UserResponseDTO savedUser = userService.getUserDetails(userId);
        return new ResponseEntity<>(savedUser,HttpStatus.OK);        
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity setUserRoles(@PathVariable("id") UUID userId,@RequestBody SetUserRolesDTO setUserRolesDTO)
    {
        UserResponseDTO savedUser = userService.setUserRoles(userId, setUserRolesDTO.getRoleIds());
        return new ResponseEntity<>(savedUser,HttpStatus.OK);
    }  
}
