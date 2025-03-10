package dev.Rajnish.EComUserAuth.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Session extends BaseModel{

    private String token;
    private Instant experingAt;
    private Instant loginAt;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private SessionStatus sessionStatus;
    
}
