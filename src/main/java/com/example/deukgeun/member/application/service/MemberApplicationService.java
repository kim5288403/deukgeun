package com.example.deukgeun.member.application.service;

import com.example.deukgeun.member.application.dto.request.JoinRequest;
import com.example.deukgeun.member.domain.aggregate.Member;

public interface MemberApplicationService {
    Member save(JoinRequest request);
    Member findById(Long id);
    Member findByEmail(String email);
}
