package com.example.deukgeun.authMail.infrastructure.persistence.adapter;

import com.example.deukgeun.authMail.domain.model.entity.AuthMail;
import com.example.deukgeun.authMail.domain.repository.AuthMailRepository;
import com.example.deukgeun.authMail.infrastructure.persistence.entity.AuthMailEntity;
import com.example.deukgeun.authMail.infrastructure.persistence.mapper.AuthMailMapper;
import com.example.deukgeun.authMail.infrastructure.persistence.repository.AuthMailJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuthMailRepositoryAdapter implements AuthMailRepository {
    private final AuthMailJpaRepository authMailRepository;
    private final AuthMailMapper authMailMapper;

    /**
     * 주어진 이메일 주소와 코드에 해당하는 인증 메일이 존재하는지 확인합니다.
     *
     * @param email 사용자 이메일 주소
     * @param code  인증 코드
     * @return 해당 이메일과 코드를 가진 인증 메일이 존재하면 true, 그렇지 않으면 false 반환
     */
    @Override
    public boolean existsByEmailAndCode(String email, String code) {
        return authMailRepository.existsByEmailAndCode(email, code);
    }

    /**
     * 주어진 이메일 주소와 일치하는 사용자가 존재하는지 확인합니다.
     *
     * @param email 사용자 이메일 주소
     * @return 해당 이메일 주소를 가진 사용자가 존재하면 true, 그렇지 않으면 false 반환
     */
    @Override
    public boolean existsByEmail(String email) {
        return authMailRepository.existsByEmail(email);
    }

    /**
     * 주어진 이메일 주소와 일치하는 인증 메일 정보를 찾습니다.
     *
     * @param email 사용자 이메일 주소
     * @return 이메일 주소와 일치하는 인증 메일 정보를 갖는 Optional 객체
     */
    @Override
    public Optional<AuthMail> findByEmail(String email) {
        Optional<AuthMailEntity> authMailEntity = authMailRepository.findByEmail(email);

        return authMailEntity.map(authMailMapper::toAuthMail);
    }

    /**
     * 주어진 이메일 주소와 코드로 인증 메일 정보를 찾습니다.
     *
     * @param email 사용자 이메일 주소
     * @param code  인증 코드
     * @return 이메일 주소와 코드로 찾은 인증 메일 정보를 갖는 Optional 객체
     */
    @Override
    public Optional<AuthMail> findByEmailAndCode(String email, String code) {
        Optional<AuthMailEntity> authMailEntity = authMailRepository.findByEmailAndCode(email, code);
        return authMailEntity.map(authMailMapper::toAuthMail);
    }

    /**
     * 주어진 이메일 주소와 일치하는 사용자의 인증 메일 정보를 삭제합니다.
     *
     * @param email 삭제할 사용자의 이메일 주소
     */
    @Override
    public void deleteByEmail(String email) {
        authMailRepository.deleteByEmail(email);
    }

    /**
     * 인증 메일 정보를 저장합니다.
     *
     * @param authMail 저장할 인증 메일 정보
     * @return 저장된 인증 메일 정보
     */
    @Override
    public AuthMail save(AuthMail authMail) {
        AuthMailEntity authMailEntity = authMailRepository.save(authMailMapper.toAuthMailEntity(authMail));
        return authMailMapper.toAuthMail(authMailEntity);
    }

    /**
     * 주어진 ID에 해당하는 인증 메일 정보를 찾습니다.
     *
     * @param id 찾을 인증 메일 정보의 고유 ID
     * @return 찾은 인증 메일 정보를 갖는 Optional 객체
     */
    @Override
    public Optional<AuthMail> findById(Long id) {
        Optional<AuthMailEntity> authMailEntity = authMailRepository.findById(id);
        return authMailEntity.map(authMailMapper::toAuthMail);
    }
}
