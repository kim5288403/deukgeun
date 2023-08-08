package com.example.deukgeun.member.application.service.implement;

import com.example.deukgeun.member.application.dto.request.JoinRequest;
import com.example.deukgeun.member.application.service.MemberApplicationService;
import com.example.deukgeun.member.domain.entity.Member;
import com.example.deukgeun.member.domain.service.MemberDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberApplicationServiceImpl implements MemberApplicationService {
    private final MemberDomainService memberDomainService;

    @Override
    public Member save(JoinRequest request) {
        return memberDomainService.save(request);
    }

    @Override
    public Member findById(Long id) {
        return memberDomainService.findById(id);
    }

    @Override
    public Member findByEmail(String email) {
        return memberDomainService.findByEmail(email);
    }


}
