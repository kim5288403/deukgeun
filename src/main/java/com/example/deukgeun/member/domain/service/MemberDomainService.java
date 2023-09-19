package com.example.deukgeun.member.domain.service;

import com.example.deukgeun.member.domain.aggregate.Member;
import com.example.deukgeun.member.domain.dto.MemberJoinDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.persistence.EntityNotFoundException;

public interface MemberDomainService {
    Member save(MemberJoinDTO memberJoinDTO);
    Member findById(Long id) throws EntityNotFoundException;
    Member findByEmail(String email) throws EntityNotFoundException;
    UserDetails loadUserByMemberUsername(String email) throws UsernameNotFoundException;
}
