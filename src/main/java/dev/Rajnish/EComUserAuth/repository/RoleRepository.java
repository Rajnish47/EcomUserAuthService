package dev.Rajnish.EComUserAuth.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.Rajnish.EComUserAuth.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,UUID> {

    Set<Role> findAllByIdIn(List<UUID> roleIds);
    Role fecthByRole(String role);
}
