package com.example.deukgeun.member.domain.repository;

import com.example.deukgeun.member.domain.aggregate.Member;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface MemberRepository{
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
    Member save(Member member);


}
