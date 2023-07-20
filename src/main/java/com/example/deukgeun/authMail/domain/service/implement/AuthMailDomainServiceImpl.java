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

    @Override
    public void confirm(AuthMailRequest request) {
        AuthMail authMail = this.findByEmailAndCode(request.getEmail(), request.getCode());
        authMail.updateMailStatus(MailStatus.Y);
        authMailRepository.save(authMail);
    }

    public void deleteByEmail(String email) {
        if (this.existsByEmail(email)) {
            authMailRepository.deleteByEmail(email);
        }
    }

    public boolean existsByEmail(String email) {
        return authMailRepository.existsByEmail(email);
    }

    public boolean existsByEmailAndCode(String email, String code) {
        return authMailRepository.existsByEmailAndCode(email, code);
    }

    public AuthMail findByEmail(String email) throws EntityNotFoundException {
        return authMailRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public AuthMail findByEmailAndCode(String email, String code) {
        return authMailRepository.findByEmailAndCode(email, code).orElseThrow(
                () -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public boolean isEmailAuthenticated(String email) {
        AuthMail authMail = this.findByEmail(email);

        return MailStatus.Y == authMail.getMailStatus();
    }

    public void save(String toEmail, String authCode) {
        AuthMail authMail = AuthMail.create(toEmail, authCode);

        authMailRepository.save(authMail);
    }
}
