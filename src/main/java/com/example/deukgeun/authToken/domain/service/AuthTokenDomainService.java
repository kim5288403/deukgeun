package com.example.deukgeun.authToken.domain.service;

import com.example.deukgeun.authToken.domain.model.entity.AuthToken;
import com.example.deukgeun.member.domain.entity.Member;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthTokenDomainService {
    void createToken(String authToken, String refreshToken);
    void deleteByAuthToken(String authToken);
    Member findMemberByEmail(String email);
    Trainer findTrainerByEmail(String email);
    AuthToken findByAuthToken(String authToken);
    UserDetails loadUserByMemberUsername(String email) throws UsernameNotFoundException;
    UserDetails loadUserByTrainerUsername(String email) throws UsernameNotFoundException;
    void updateAuthToken(String authToken, String newAuthToken);
}
