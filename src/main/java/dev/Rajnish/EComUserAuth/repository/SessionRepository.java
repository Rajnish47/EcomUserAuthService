package dev.Rajnish.EComUserAuth.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.Rajnish.EComUserAuth.entity.Session;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session,UUID> {

    Optional<Session> findByTokenAndUser_Id(String token, UUID user_Id);    
}
