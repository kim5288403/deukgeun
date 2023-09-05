package com.example.deukgeun.authMail.domain.service.implement;

import com.example.deukgeun.authMail.application.dto.request.AuthMailRequest;
import com.example.deukgeun.authMail.domain.model.entity.AuthMail;
import com.example.deukgeun.authMail.domain.model.valueobject.MailStatus;
import com.example.deukgeun.authMail.domain.repository.AuthMailRepository;
import com.example.deukgeun.authMail.domain.service.AuthMailDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class AuthMailDomainServiceImpl implements AuthMailDomainService {

    private final AuthMailRepository authMailRepository;

    /**
     * 이메일 주소와 인증 코드를 사용하여 인증 메일을 확인하고 상태를 업데이트합니다.
     *
     * @param request 인증 메일 요청 객체
     */
    @Override
    public void confirm(AuthMailRequest request) {
        AuthMail authMail = this.findByEmailAndCode(request.getEmail(), request.getCode());
        authMail.updateMailStatus(MailStatus.Y);
        authMailRepository.save(authMail);
    }

    /**
     * 주어진 이메일 주소와 연관된 인증 메일 정보를 삭제합니다.
     *
     * @param email 삭제할 이메일 주소
     */
    @Override
    public void deleteByEmail(String email) {
        if (this.existsByEmail(email)) {
            authMailRepository.deleteByEmail(email);
        }
    }

    /**
     * 주어진 이메일 주소와 관련된 인증 메일 정보가 존재하는지 확인합니다.
     *
     * @param email 확인할 이메일 주소
     * @return 인증 메일 정보가 존재하면 true, 그렇지 않으면 false
     */
    @Override
    public boolean existsByEmail(String email) {
        return authMailRepository.existsByEmail(email);
    }

    /**
     * 주어진 이메일 주소와 인증 코드가 일치하는 인증 메일 정보가 존재하는지 확인합니다.
     *
     * @param email 확인할 이메일 주소
     * @param code 확인할 인증 코드
     * @return 인증 메일 정보가 존재하면 true, 그렇지 않으면 false
     */
    @Override
    public boolean existsByEmailAndCode(String email, String code) {
        return authMailRepository.existsByEmailAndCode(email, code);
    }

    /**
     * 주어진 이메일 주소와 연관된 인증 메일 정보를 검색합니다.
     *
     * @param email 검색할 이메일 주소
     * @return 이메일 주소와 연관된 인증 메일 정보
     * @throws EntityNotFoundException 이메일 주소에 대한 정보를 찾을 수 없는 경우 발생
     */
    @Override
    public AuthMail findByEmail(String email) throws EntityNotFoundException {
        return authMailRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    /**
     * 주어진 이메일 주소와 인증 코드와 연관된 인증 메일 정보를 검색합니다.
     *
     * @param email 검색할 이메일 주소
     * @param code 검색할 인증 코드
     * @return 검색된 인증 메일 정보
     * @throws EntityNotFoundException 사용자를 찾을 수 없는 경우 발생
     */
    @Override
    public AuthMail findByEmailAndCode(String email, String code) {
        return authMailRepository.findByEmailAndCode(email, code).orElseThrow(
                () -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    /**
     * 주어진 이메일 주소에 대한 이메일 인증 상태를 확인합니다.
     *
     * @param email 확인할 이메일 주소
     * @return 이메일이 인증되었다면 true, 그렇지 않으면 false
     * @throws EntityNotFoundException 사용자를 찾을 수 없는 경우 발생
     */
    @Override
    public boolean isEmailAuthenticated(String email) {
        AuthMail authMail = this.findByEmail(email);

        return authMail.isEmailAuthenticated();
    }

    /**
     * 주어진 이메일 주소와 인증 코드로 인증 메일 정보를 생성하고 저장합니다.
     *
     * @param toEmail 수신 이메일 주소
     * @param authCode 생성된 인증 코드
     */
    @Override
    public void save(String toEmail, String authCode) {
        AuthMail authMail = AuthMail.create(toEmail, authCode);

        authMailRepository.save(authMail);
    }
}
