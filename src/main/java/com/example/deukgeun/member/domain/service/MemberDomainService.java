package com.example.deukgeun.member.domain.service;

import com.example.deukgeun.member.application.dto.request.JoinRequest;
import com.example.deukgeun.member.domain.entity.Member;

public interface MemberDomainService {
    Member save(JoinRequest request);
    Member findByEmail(String email);
}
