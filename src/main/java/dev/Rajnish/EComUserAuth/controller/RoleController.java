package dev.Rajnish.EComUserAuth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.Rajnish.EComUserAuth.dto.RoleResponseDTO;
import dev.Rajnish.EComUserAuth.service.interfaces.RoleService;



@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/create/{name}")
    public ResponseEntity<RoleResponseDTO> createNewRole(@PathVariable("name") String roleName)
    {
        RoleResponseDTO savedRole = roleService.createRole(roleName);
        return new ResponseEntity<>(savedRole,HttpStatus.OK);        
    }    
}
