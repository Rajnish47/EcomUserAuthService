package dev.Rajnish.EComUserAuth.dto;

import dev.Rajnish.EComUserAuth.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDTO {

    private String name;
    private String email;
    private String password;

    public static User createUser(SignUpRequestDTO signUpRequestDTO)
    {
        User user = new User();
        user.setName(signUpRequestDTO.getName());
        user.setEmail(signUpRequestDTO.getEmail());
        
        return user;
    }
}
