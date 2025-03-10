package dev.Rajnish.EComUserAuth.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="ECOM_USER")
public class User extends BaseModel {

    private String name;
    private String email;
    private String password;
    @ManyToMany
    private Set<Role> userRoles;   
}
