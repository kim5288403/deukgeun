package com.example.deukgeun.auth.infrastructure.persistence.repository;

import com.example.deukgeun.auth.infrastructure.persistence.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long>{

  Optional<Token> findByAuthToken(String authToken);
  
  @Transactional
  void deleteByAuthToken(String authToken);
}
