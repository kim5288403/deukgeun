package com.example.deukgeun.trainer.application.controller;


import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.request.WithdrawalUserRequest;
import com.example.deukgeun.trainer.application.dto.response.TrainerResponse;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;


@RestController
@RequestMapping("/api/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerApplicationService trainerApplicationService;
    private final AuthTokenApplicationService authTokenApplicationService;

    @RequestMapping(method = RequestMethod.GET, path = "/detail")
    public ResponseEntity<?> getDetail(HttpServletRequest request) {
        // 인증 토큰에서 사용자의 인증 정보를 추출
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        Trainer trainer = trainerApplicationService.findByEmail(email);

        // 사용자 정보를 응답 객체로 변환
        TrainerResponse response = new TrainerResponse(trainer);

        return RestResponseUtil.ok("마이 페이지 조회 성공했습니다.", response);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(@Valid JoinRequest request, BindingResult bindingResult) throws IOException {
        // 사용자 저장
        Trainer saveTrainer = trainerApplicationService.save(request);

        return RestResponseUtil.ok("회원 가입 성공 했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/")
    public ResponseEntity<?> update(@Valid UpdateInfoRequest request, BindingResult bindingResult) {
        // 정보 업데이트
        trainerApplicationService.updateInfo(request);

        return RestResponseUtil.ok("내 정보 수정 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/password")
    public ResponseEntity<?> updatePassword(
            @Valid UpdatePasswordRequest request,
            BindingResult bindingResult) {
        // 비밀번호 업데이트
        trainerApplicationService.updatePassword(request);

        return RestResponseUtil.ok("비밀번호 변경 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/")
    public ResponseEntity<?> withdrawal(
            HttpServletRequest request,
            @Valid WithdrawalUserRequest withdrawalRequest,
            BindingResult bindingResult
    ) throws IOException {
        //사용자 삭제
        trainerApplicationService.delete(withdrawalRequest.getEmail());

        //토큰 삭제
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        authTokenApplicationService.deleteByAuthToken(authToken);

        return RestResponseUtil
                .ok("회원 탈퇴 성공했습니다.", null);
    }
}

