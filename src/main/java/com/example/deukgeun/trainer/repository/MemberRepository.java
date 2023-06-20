package com.example.deukgeun.trainer.repository;


import com.example.deukgeun.trainer.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByEmail(String email);
   
  boolean existsByEmail(String email);
}
