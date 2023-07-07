package com.example.deukgeun.member.service.implement;

import com.example.deukgeun.global.exception.PasswordMismatchException;
import com.example.deukgeun.global.entity.Member;
import com.example.deukgeun.global.repository.MemberRepository;
import com.example.deukgeun.member.request.JoinRequest;
import com.example.deukgeun.member.service.MemberService;
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
     * 인증을 위해 제공된 비밀번호가 사용자의 비밀번호와 일치하는지 확인합니다.
     *
     * @param member 이메일과 비밀번호가 포함된 객체
     * @param password 로그인 요청 비밀번호
     * @throws PasswordMismatchException 비밀번호가 사용자의 비밀번호와 일치하지 않는 경우 예외가 발생합니다.
     */
    public void isPasswordMatches(String password, Member member) throws PasswordMismatchException {
        boolean check = passwordEncoder.matches(password, member.getPassword());

        if (!check) {
            throw new PasswordMismatchException("사용자를 찾을 수 없습니다.");
        }
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
