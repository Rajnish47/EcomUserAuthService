package dev.Rajnish.EComUserAuth.dto;

import java.util.UUID;

import dev.Rajnish.EComUserAuth.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleResponseDTO {

    private UUID roleId;
    private String role;

    public static RoleResponseDTO createRoleResponseDTO(Role savedRole)
    {
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO();
        roleResponseDTO.setRole(savedRole.getName());
        roleResponseDTO.setRoleId(savedRole.getId());

        return roleResponseDTO;
    }
}
