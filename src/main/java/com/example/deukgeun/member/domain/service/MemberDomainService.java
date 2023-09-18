package com.example.deukgeun.member.domain.service;

import com.example.deukgeun.member.application.dto.request.JoinRequest;
import com.example.deukgeun.member.domain.aggregate.Member;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.EntityNotFoundException;

public interface MemberDomainService {
    Member save(JoinRequest request);
    Member findById(Long id) throws EntityNotFoundException;
    Member findByEmail(String email) throws EntityNotFoundException;
    UserDetails loadUserByMemberUsername(String email) throws UsernameNotFoundException;
}
