package dev.Rajnish.EComUserAuth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.Rajnish.EComUserAuth.entity.Role;
import dev.Rajnish.EComUserAuth.repository.RoleRepository;
import dev.Rajnish.EComUserAuth.service.interfaces.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role createRole(String name) {

        Role role = new Role();
        role.setRole(name);
        Role savedRole = roleRepository.save(role);

        return savedRole;
    }    
}
