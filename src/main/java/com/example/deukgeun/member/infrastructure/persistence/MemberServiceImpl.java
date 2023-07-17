package com.example.deukgeun.member.infrastructure.persistence;

import com.example.deukgeun.member.domain.entity.Member;
import com.example.deukgeun.member.domain.repository.MemberRepository;
import com.example.deukgeun.member.application.dto.request.JoinRequest;
import com.example.deukgeun.member.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

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

    /**
     * 주어진 이메일을 기반으로 사용자를 조회합니다.
     *
     * @param email 사용자의 이메일
     * @return 조회된 사용자
     * @throws EntityNotFoundException 주어진 이메일에 해당하는 사용자가 없는 경우 발생하는 예외
     */
    public Member getByEmail(String email) throws EntityNotFoundException {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
