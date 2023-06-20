package com.example.deukgeun.commom.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "token")
@NoArgsConstructor
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name = "auth_token", length = 500, nullable = false)
  private String authToken;
  
  @Column(name = "refresh_token", length = 500, nullable = false)
  private String refreshToken;
  
  @Builder
  public Token(String authToken,String refreshToken) {
    this.authToken = authToken;
    this.refreshToken = refreshToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }
  
}
