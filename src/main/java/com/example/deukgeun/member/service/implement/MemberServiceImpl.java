package com.example.deukgeun.member.service.implement;

import com.example.deukgeun.member.entity.Member;
import com.example.deukgeun.member.repository.MemberRepository;
import com.example.deukgeun.member.request.JoinRequest;
import com.example.deukgeun.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member save(JoinRequest request) {

        Member member = Member
                .builder()
                .name(request.getName())
                .age(request.getAge())
                .gender(request.getGender())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        return memberRepository.save(member);
    }
}
