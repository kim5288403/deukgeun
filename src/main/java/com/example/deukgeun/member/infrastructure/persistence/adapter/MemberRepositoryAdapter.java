package com.example.deukgeun.member.infrastructure.persistence.adapter;

import com.example.deukgeun.member.domain.entity.Member;
import com.example.deukgeun.member.domain.repository.MemberRepository;
import com.example.deukgeun.member.infrastructure.persistence.entity.MemberEntity;
import com.example.deukgeun.member.infrastructure.persistence.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MemberRepositoryAdapter implements MemberRepository {
    private final MemberJpaRepository memberRepository;

    @Override
    public List<Member> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        return memberEntityList.stream().map(this::covert).collect(Collectors.toList());
    }

    @Override
    public Optional<Member> findById(Long id) {
        Optional<MemberEntity> memberEntity = memberRepository.findById(id);
        return memberEntity.map(this::covert);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        Optional<MemberEntity> memberEntity = memberRepository.findByEmail(email);
        return memberEntity.map(this::covert);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

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
