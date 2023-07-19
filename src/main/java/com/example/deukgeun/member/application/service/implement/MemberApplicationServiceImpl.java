package com.example.deukgeun.member.application.service.implement;

import com.example.deukgeun.member.application.dto.request.JoinRequest;
import com.example.deukgeun.member.application.service.MemberApplicationService;
import com.example.deukgeun.member.domain.entity.Member;
import com.example.deukgeun.member.domain.service.implement.MemberDomainServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class MemberApplicationServiceImpl implements MemberApplicationService {
    private MemberDomainServiceImpl memberDomainService;

    public Member save(JoinRequest request) {
        return memberDomainService.save(request);
    }

    /**
     * 주어진 이메일을 기반으로 사용자를 조회합니다.
     *
     * @param email 사용자의 이메일
     * @return 조회된 사용자
     * @throws EntityNotFoundException 주어진 이메일에 해당하는 사용자가 없는 경우 발생하는 예외
     */
    public Member findByEmail(String email) throws EntityNotFoundException {
        return memberDomainService.findByEmail(email);
    }
}
