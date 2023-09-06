package com.example.deukgeun.member.domain.service.implement;

import com.example.deukgeun.global.util.PasswordEncoderUtil;
import com.example.deukgeun.member.application.dto.request.JoinRequest;
import com.example.deukgeun.member.domain.entity.Member;
import com.example.deukgeun.member.domain.repository.MemberRepository;
import com.example.deukgeun.member.domain.service.MemberDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class MemberDomainServiceImpl implements MemberDomainService {

    private final MemberRepository memberRepository;

    @Override
    public Member save(JoinRequest request) {
        Member member = Member
                .create(request.getEmail(),
                        PasswordEncoderUtil.encode(request.getPassword()),
                        request.getName(),
                        request.getAge(),
                        request.getGender()
                );

        return memberRepository.save(member);
    }

    @Override
    public Member findById(Long id) throws EntityNotFoundException {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("없는 정보 입니다."));
    }

    @Override
    public Member findByEmail(String email) throws EntityNotFoundException {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public UserDetails loadUserByMemberUsername(String email) throws UsernameNotFoundException {
        return memberRepository.loadUserByUsername(email);
    }
}
