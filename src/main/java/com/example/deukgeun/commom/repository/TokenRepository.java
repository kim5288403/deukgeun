package com.example.deukgeun.commom.repository;

import com.example.deukgeun.commom.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long>{

  Optional<Token> findByAuthToken(String authToken);
  
  @Transactional
  void deleteByAuthToken(String authToken);
  
  @Transactional
  @Modifying
  @Query("update Token m set m.authToken = :newAuthToken where m.authToken = :authToken")
  void updateAuthToken(@Param(value = "authToken")String authToken, @Param(value = "newAuthToken")String newAuthToken);
}
