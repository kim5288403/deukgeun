package com.example.deukgeun.member.application.service;

import com.example.deukgeun.member.application.dto.request.JoinRequest;
import com.example.deukgeun.member.domain.entity.Member;

public interface MemberApplicationService {
    Member save(JoinRequest request);
    Member findByEmail(String email);
}
