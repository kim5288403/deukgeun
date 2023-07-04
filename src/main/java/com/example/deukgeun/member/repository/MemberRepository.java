package com.example.deukgeun.member.repository;

import com.example.deukgeun.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>{
}
