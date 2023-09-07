package com.example.deukgeun.member.infrastructure.persistence.adapter;

import com.example.deukgeun.member.domain.entity.Member;
import com.example.deukgeun.member.domain.repository.MemberRepository;
import com.example.deukgeun.member.infrastructure.persistence.entity.MemberEntity;
import com.example.deukgeun.member.infrastructure.persistence.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberRepositoryAdapter implements MemberRepository {
    private final MemberJpaRepository memberRepository;

    /**
     * 회원 식별자를 사용하여 회원 정보를 조회합니다.
     *
     * @param id 조회할 회원의 식별자입니다.
     * @return 조회된 회원 정보를 나타내는 Optional 객체입니다.
     */
    @Override
    public Optional<Member> findById(Long id) {
        Optional<MemberEntity> memberEntity = memberRepository.findById(id);
        return memberEntity.map(this::covert);
    }

    /**
     * 이메일 주소를 사용하여 회원 정보를 조회합니다.
     *
     * @param email 조회할 회원의 이메일 주소입니다.
     * @return 조회된 회원 정보를 나타내는 Optional 객체입니다.
     */
    @Override
    public Optional<Member> findByEmail(String email) {
        Optional<MemberEntity> memberEntity = memberRepository.findByEmail(email);
        return memberEntity.map(this::covert);
    }

    /**
     * 이메일 주소를 사용하여 사용자 정보를 로드합니다.
     *
     * @param email 로드할 사용자의 이메일 주소입니다.
     * @return 로드된 사용자 정보를 나타내는 UserDetails 객체입니다.
     * @throws UsernameNotFoundException 만일 해당 이메일 주소로 사용자를 찾을 수 없는 경우 발생합니다.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    /**
     * 회원 정보를 저장합니다.
     *
     * @param member 저장할 회원 정보를 나타내는 Member 객체입니다.
     * @return 저장된 회원 정보를 나타내는 Member 객체입니다.
     */
    @Override
    public Member save(Member member) {
        MemberEntity memberEntity = covert(member);
        MemberEntity saveMemberEntity = memberRepository.save(memberEntity);
        return covert(saveMemberEntity);
    }

    private MemberEntity covert(Member member) {
        return MemberEntity
                .builder()
                .id(member.getId())
                .name(member.getName())
                .age(member.getAge())
                .password(member.getPassword())
                .email(member.getEmail())
                .gender(member.getGender())
                .build();
    }

    private Member covert(MemberEntity memberEntity) {
        return new Member(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getName(),
                memberEntity.getAge(),
                memberEntity.getGender()
        );
    }
}
