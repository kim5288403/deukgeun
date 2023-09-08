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

    /**
     * 현재 로그인한 사용자의 트레이너 정보를 조회하는 엔드포인트입니다.
     *
     * @param request HttpServletRequest 객체로, 인증 토큰을 추출하기 위해 사용됩니다.
     * @return 현재 로그인한 사용자의 트레이너 정보에 대한 응답 객체입니다.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/info")
    public ResponseEntity<?> getInfo(HttpServletRequest request) {
        // 요청에서 인증 토큰을 추출합니다.
        String authToken = authTokenApplicationService.resolveAuthToken(request);

        // 추출한 인증 토큰을 사용하여 사용자의 이메일 정보를 얻습니다.
        String email = authTokenApplicationService.getUserPk(authToken);

        // 이메일 정보를 기반으로 해당 사용자의 트레이너 정보를 조회합니다.
        Trainer trainer = trainerApplicationService.findByEmail(email);

        // 조회된 트레이너 정보를 트레이너 응답 객체로 변환합니다.
        TrainerResponse.Info response = new TrainerResponse.Info(trainer);

        return RestResponseUtil.ok("마이 페이지 조회 성공했습니다.", response);
    }

    /**
     * 현재 로그인한 사용자의 트레이너 상세 정보를 조회하는 엔드포인트입니다.
     *
     * @param request HttpServletRequest 객체로, 인증 토큰을 추출하기 위해 사용됩니다.
     * @return 현재 로그인한 사용자의 트레이너 상세 정보에 대한 응답 객체입니다.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/detail")
    public ResponseEntity<?> getDetail(HttpServletRequest request) {
        // 요청에서 인증 토큰을 추출합니다.
        String authToken = authTokenApplicationService.resolveAuthToken(request);

        // 추출한 인증 토큰을 사용하여 사용자의 이메일 정보를 얻습니다.
        String email = authTokenApplicationService.getUserPk(authToken);

        // 이메일 정보를 기반으로 해당 사용자의 트레이너 정보를 조회합니다.
        Trainer trainer = trainerApplicationService.findByEmail(email);

        // 조회된 트레이너 정보를 트레이너 상세 응답 객체로 변환합니다.
        TrainerResponse.Detail response = new TrainerResponse.Detail(trainer);

        return RestResponseUtil.ok("마이 페이지 조회 성공했습니다.", response);
    }


    /**
     * 새로운 트레이너 회원을 등록하는 엔드포인트입니다.
     *
     * @param request JoinRequest 객체로, 새로운 트레이너 회원의 정보를 포함합니다.
     * @param bindingResult BindingResult 객체로, 데이터 유효성 검사 결과를 저장합니다.
     * @return 회원 등록 완료 메시지를 포함한 응답 객체입니다.
     * @throws IOException 파일 업로드 중 예외가 발생할 경우 던집니다.
     */
    @Transactional
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(@Valid JoinRequest request, BindingResult bindingResult) throws IOException {
        // JoinRequest 객체를 사용하여 새로운 트레이너 회원을 등록하고 저장합니다.
        Trainer saveTrainer = trainerApplicationService.save(request);

        return RestResponseUtil.ok("회원 가입 성공 했습니다.", null);
    }

    /**
     * 현재 트레이너 회원의 정보를 업데이트하는 엔드포인트입니다.
     *
     * @param request UpdateInfoRequest 객체로, 업데이트할 정보를 포함합니다.
     * @param bindingResult BindingResult 객체로, 데이터 유효성 검사 결과를 저장합니다.
     * @return 정보 업데이트 완료 메시지를 포함한 응답 객체입니다.
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/")
    public ResponseEntity<?> update(@Valid UpdateInfoRequest request, BindingResult bindingResult) {
        // UpdateInfoRequest 객체를 사용하여 현재 트레이너 회원의 정보를 업데이트합니다.
        trainerApplicationService.updateInfo(request);

        return RestResponseUtil.ok("내 정보 수정 성공했습니다.", null);
    }

    /**
     * 현재 트레이너 회원의 비밀번호를 업데이트하는 엔드포인트입니다.
     *
     * @param request UpdatePasswordRequest 객체로, 새로운 비밀번호 정보를 포함합니다.
     * @param bindingResult BindingResult 객체로, 데이터 유효성 검사 결과를 저장합니다.
     * @return 비밀번호 업데이트 완료 메시지를 포함한 응답 객체입니다.
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/password")
    public ResponseEntity<?> updatePassword(
            @Valid UpdatePasswordRequest request,
            BindingResult bindingResult) {
        // UpdatePasswordRequest 객체를 사용하여 현재 트레이너 회원의 비밀번호를 업데이트합니다.
        trainerApplicationService.updatePassword(request);

        return RestResponseUtil.ok("비밀번호 변경 성공했습니다.", null);
    }

    /**
     * 회원 탈퇴를 처리하는 엔드포인트입니다.
     *
     * @param request           HttpServletRequest 객체로, 현재 요청에 대한 정보를 제공합니다.
     * @param withdrawalRequest WithdrawalUserRequest 객체로, 탈퇴할 회원의 이메일을 포함합니다.
     * @param bindingResult     BindingResult 객체로, 데이터 유효성 검사 결과를 저장합니다.
     * @return 회원 탈퇴 완료 메시지를 포함한 응답 객체입니다.
     * @throws IOException 입출력 예외가 발생할 경우 처리합니다.
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/")
    public ResponseEntity<?> withdrawal(
            HttpServletRequest request,
            @Valid WithdrawalUserRequest withdrawalRequest,
            BindingResult bindingResult
    ) throws IOException {
        // WithdrawalUserRequest 객체를 사용하여 회원을 탈퇴 처리합니다.
        trainerApplicationService.delete(withdrawalRequest.getEmail());

        // 현재 요청의 Authorization 헤더로부터 인증 토큰을 추출합니다.
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        // 추출한 인증 토큰을 사용하여 해당 사용자의 세션을 삭제합니다.
        authTokenApplicationService.deleteByAuthToken(authToken);

        // 회원 탈퇴 성공 메시지를 응답 객체에 담아 반환합니다.
        return RestResponseUtil.ok("회원 탈퇴 성공했습니다.", null);
    }
}

