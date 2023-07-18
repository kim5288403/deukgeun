package com.example.deukgeun.auth.domain.service.implement;

import com.example.deukgeun.auth.domain.model.entity.AuthMail;
import com.example.deukgeun.auth.domain.model.valueobject.MailStatus;
import com.example.deukgeun.auth.domain.repository.AuthMailRepository;
import com.example.deukgeun.auth.domain.service.AuthMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthMailServiceImpl implements AuthMailService {

    private final AuthMailRepository authMailRepository;

    public void save(String toEmail, String authCode) {
        AuthMail authMail = AuthMail.create(toEmail, authCode);

        authMailRepository.save(authMail);
    }

    public void deleteByEmail(String email) {
        authMailRepository.deleteByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return authMailRepository.existsByEmail(email);
    }

    public boolean existsByEmailAndCode(String email, String code) {
        return authMailRepository.existsByEmailAndCode(email, code);
    }

    public Optional<AuthMail> findByEmail(String email) throws EntityNotFoundException {
        return authMailRepository.findByEmail(email);
    }

    @Override
    public void updateMailStatus(MailStatus mailStatus, AuthMail authMail) {
        authMail.updateMailStatus(mailStatus);
        authMailRepository.save(authMail);
    }


}
