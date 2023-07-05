package com.example.deukgeun.member.repository;

import com.example.deukgeun.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{
    Optional<Member> findByEmail(String email);
}
