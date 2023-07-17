package com.example.deukgeun.auth.infrastructure.repository;

import com.example.deukgeun.auth.domain.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long>{

  Optional<Token> findByAuthToken(String authToken);
  
  @Transactional
  void deleteByAuthToken(String authToken);
}
