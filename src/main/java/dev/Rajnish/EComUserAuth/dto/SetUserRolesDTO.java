package dev.Rajnish.EComUserAuth.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetUserRolesDTO {
    
    private List<UUID> roleIds;
}
