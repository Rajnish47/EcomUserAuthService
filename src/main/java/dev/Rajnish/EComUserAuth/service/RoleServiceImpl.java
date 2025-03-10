package dev.Rajnish.EComUserAuth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.Rajnish.EComUserAuth.dto.RoleResponseDTO;
import dev.Rajnish.EComUserAuth.entity.Role;
import dev.Rajnish.EComUserAuth.repository.RoleRepository;
import dev.Rajnish.EComUserAuth.service.interfaces.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleResponseDTO createRole(String name) {

        Role role = new Role();
        role.setName(name);
        Role savedRole = roleRepository.save(role);

        return RoleResponseDTO.createRoleResponseDTO(savedRole);
    }

    @Override
    public Role fetchByName(String roleName) {

        Role savedRole = roleRepository.getRoleByName(roleName);
        return savedRole;
    }   
}
