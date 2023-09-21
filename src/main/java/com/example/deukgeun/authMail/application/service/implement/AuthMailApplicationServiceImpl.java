package com.example.deukgeun.authMail.application.service.implement;

import com.example.deukgeun.authMail.application.dto.request.AuthMailRequest;
import com.example.deukgeun.authMail.application.service.AuthMailApplicationService;
import com.example.deukgeun.authMail.domain.dto.ConfirmDTO;
import com.example.deukgeun.authMail.domain.service.AuthMailDomainService;
import com.example.deukgeun.authMail.infrastructure.persistence.mapper.AuthMailMapper;
import com.example.deukgeun.global.util.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class AuthMailApplicationServiceImpl implements AuthMailApplicationService {


    private final AuthMailDomainService authMailDomainService;
    private final AuthMailMapper authMailMapper;

    /**
     * 메일 인증 요청을 확인하고 처리합니다.
     *
     * @param request 메일 인증 요청 객체
     */
    public void confirm(AuthMailRequest request) {
        ConfirmDTO confirmDTO = authMailMapper.toConfirmDto(request);
        authMailDomainService.confirm(confirmDTO);
    }

    @Override
    public String createCode() {
        return MailUtil.createCode();
    }

    /**
     * 주어진 이메일 주소와 관련된 인증 메일 정보를 삭제합니다.
     *
     * @param email 삭제할 이메일 주소
     */
    public void deleteByEmail(String email) {
        if (authMailDomainService.existsByEmail(email)) {
            authMailDomainService.deleteByEmail(email);
        }
    }

    /**
     * 주어진 이메일 주소와 인증 코드가 일치하는 인증 메일 정보가 존재하는지 확인합니다.
     *
     * @param email 이메일 주소
     * @param code 인증 코드
     * @return 인증 메일 정보가 존재하면 true, 그렇지 않으면 false
     */
    public boolean existsByEmailAndCode(String email, String code) {
        return authMailDomainService.existsByEmailAndCode(email, code);
    }

    /**
     * 주어진 이메일 주소에 대한 인증 상태를 확인합니다.
     *
     * @param email 확인할 이메일 주소
     * @return 이메일이 인증되었다면 true, 그렇지 않으면 false
     * @throws EntityNotFoundException 이메일에 대한 정보를 찾을 수 없는 경우 발생
     */
    public boolean isEmailAuthenticated(String email) throws EntityNotFoundException {
        return authMailDomainService.isEmailAuthenticated(email);
    }

    /**
     * 인증 메일 요청 정보를 저장합니다.
     *
     * @param authMailRequest 저장할 인증 메일 요청 객체
     */
    public void save(AuthMailRequest authMailRequest) {
        authMailDomainService.save(authMailRequest.getEmail(), authMailRequest.getCode());
    }
}
