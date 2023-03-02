package com.example.deukgeun.commom.repository;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.deukgeun.commom.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long>{
  Optional<Token> findByAuthToken(String authToken);
  
  @Transactional
  void deleteByAuthToken(String authToken);
  
  @Transactional
  @Modifying
  @Query("update Token m set m.authToken = :newAuthToken where m.authToken = :authToken")
  int updateAuthToken(@Param(value = "authToken")String authToken, @Param(value = "newAuthToken")String newAuthToken);
}
