package com.example.deukgeun.authToken.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "auth_token")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "auth_token", length = 500, nullable = false)
  private String authToken;

  @Column(name = "refresh_token", length = 500, nullable = false)
  private String refreshToken;
}
