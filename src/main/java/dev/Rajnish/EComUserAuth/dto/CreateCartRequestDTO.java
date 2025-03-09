package dev.Rajnish.EComUserAuth.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCartRequestDTO {

    private UUID userId;
    private String cartName;    
}
