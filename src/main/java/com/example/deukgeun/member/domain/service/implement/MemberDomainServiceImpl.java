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

    /**
     * 회원 정보를 저장합니다.
     *
     * @param request 저장할 회원 정보를 포함하는 JoinRequest 객체입니다.
     * @return 저장된 회원 정보를 나타내는 객체입니다.
     */
    @Override
    public Member save(JoinRequest request) {
        // 회원 생성 메서드를 호출하여 회원 객체를 생성하고 저장합니다.
        Member member = Member
                .create(request.getEmail(),
                        PasswordEncoderUtil.encode(request.getPassword()),
                        request.getName(),
                        request.getAge(),
                        request.getGender()
                );

        return memberRepository.save(member);
    }

    /**
     * 회원 식별자를 사용하여 회원 정보를 조회합니다.
     *
     * @param id 조회할 회원의 식별자입니다.
     * @return 조회된 회원 정보를 나타내는 객체입니다.
     * @throws EntityNotFoundException 만일 해당 식별자로 회원을 찾을 수 없는 경우 발생합니다.
     */
    @Override
    public Member findById(Long id) throws EntityNotFoundException {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("없는 정보 입니다."));
    }

    /**
     * 이메일 주소를 사용하여 회원 정보를 조회합니다.
     *
     * @param email 조회할 회원의 이메일 주소입니다.
     * @return 조회된 회원 정보를 나타내는 객체입니다.
     * @throws EntityNotFoundException 만일 해당 이메일 주소로 회원을 찾을 수 없는 경우 발생합니다.
     */
    @Override
    public Member findByEmail(String email) throws EntityNotFoundException {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    /**
     * 이메일 주소를 사용하여 사용자 정보를 로드합니다.
     *
     * @param email 로드할 사용자의 이메일 주소입니다.
     * @return 로드된 사용자 정보를 나타내는 UserDetails 객체입니다.
     * @throws UsernameNotFoundException 만일 해당 이메일 주소로 사용자를 찾을 수 없는 경우 발생합니다.
     */
    @Override
    public UserDetails loadUserByMemberUsername(String email) throws UsernameNotFoundException {
        return memberRepository.loadUserByUsername(email);
    }
}
