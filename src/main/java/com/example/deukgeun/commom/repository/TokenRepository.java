package com.example.deukgeun.commom.repository;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.deukgeun.commom.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long>{
  Optional<Token> findByAuthToken(String authToken);
  
  @Transactional
  void deleteByAuthToken(String authToken);
}
