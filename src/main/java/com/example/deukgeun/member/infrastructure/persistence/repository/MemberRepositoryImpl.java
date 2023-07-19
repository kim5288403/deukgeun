package com.example.deukgeun.member.infrastructure.persistence.repository;

import com.example.deukgeun.member.infrastructure.persistence.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepositoryImpl extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByEmail(String email);
}
