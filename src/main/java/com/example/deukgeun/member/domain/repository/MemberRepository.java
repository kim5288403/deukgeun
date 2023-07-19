package com.example.deukgeun.member.domain.repository;

import com.example.deukgeun.member.domain.entity.Member;

import java.util.Optional;

public interface MemberRepository{
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    Member save(Member member);


}
