package dev.Rajnish.EComUserAuth.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.Rajnish.EComUserAuth.entity.User;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,UUID> {

    Optional<User> findByEmail(String email);    
}
