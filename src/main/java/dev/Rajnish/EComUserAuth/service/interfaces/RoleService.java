package dev.Rajnish.EComUserAuth.service.interfaces;

import dev.Rajnish.EComUserAuth.dto.RoleResponseDTO;
import dev.Rajnish.EComUserAuth.entity.Role;

public interface RoleService {

    RoleResponseDTO createRole(String name);
    Role fetchByName(String roleName);
}
