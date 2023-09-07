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

    /**
     * 회원 정보를 저장합니다.
     *
     * @param request 저장할 회원 정보를 포함하는 JoinRequest 객체입니다.
     * @return 저장된 회원 객체입니다.
     */
    @Override
    public Member save(JoinRequest request) {
        return memberDomainService.save(request);
    }

    /**
     * 회원 식별자를 사용하여 회원 정보를 조회합니다.
     *
     * @param id 조회할 회원의 식별자입니다.
     * @return 조회된 회원 정보를 나타내는 객체입니다.
     */
    @Override
    public Member findById(Long id) {
        return memberDomainService.findById(id);
    }

    /**
     * 이메일 주소를 사용하여 회원 정보를 조회합니다.
     *
     * @param email 조회할 회원의 이메일 주소입니다.
     * @return 조회된 회원 정보를 나타내는 객체입니다.
     */
    @Override
    public Member findByEmail(String email) {
        return memberDomainService.findByEmail(email);
    }
}
