package dev.Rajnish.EComUserAuth.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dev.Rajnish.EComUserAuth.entity.Role;
import dev.Rajnish.EComUserAuth.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {

    private UUID id;
    private String name;
    private String email;
    private List<RoleResponseDTO> userRoles;

    public static UserResponseDTO createUserResponseDTO(User user)
    {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setName(user.getName());;
        List<RoleResponseDTO> roleResponseDTOs = new ArrayList<>();
        
        for(Role role:user.getUserRoles())
        {
            RoleResponseDTO roleResponseDTO = RoleResponseDTO.createRoleResponseDTO(role);
            roleResponseDTOs.add(roleResponseDTO);
        }
        userResponseDTO.setUserRoles(roleResponseDTOs);

        return userResponseDTO;        
    }
}
