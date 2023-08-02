package com.example.deukgeun.authToken.infrastructure.persistence.repository;

import com.example.deukgeun.authToken.infrastructure.persistence.entity.AuthTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthTokenJpaRepository extends JpaRepository<AuthTokenEntity, Long> {
    void deleteByAuthToken(String authToken);
    Optional<AuthTokenEntity> findByAuthToken(String authToken);
}
