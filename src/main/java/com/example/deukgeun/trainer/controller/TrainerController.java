package com.example.deukgeun.trainer.controller;


import com.example.deukgeun.main.request.LoginRequest;
import com.example.deukgeun.main.response.LoginResponse;
import com.example.deukgeun.main.service.implement.TokenServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.global.entity.Profile;
import com.example.deukgeun.global.entity.Trainer;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.request.WithdrawalUserRequest;
import com.example.deukgeun.trainer.response.TrainerResponse;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import com.example.deukgeun.trainer.service.implement.TrainerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;


@RestController
@RequestMapping("/api/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerServiceImpl trainerService;
    private final TokenServiceImpl tokenService;
    private final ProfileServiceImpl profileService;

    @Value("${deukgeun.role.trainer}")
    private String role;

    /**
     * 사용자 상세 정보를 조회합니다.
     *
     * @param request HttpServletRequest 객체
     * @return ResponseEntity 객체
     */
    @RequestMapping(method = RequestMethod.GET, path = "/detail")
    public ResponseEntity<?> getDetail(HttpServletRequest request) {
        // 인증 토큰에서 사용자의 인증 정보를 추출
        String authToken = tokenService.resolveAuthToken(request);
        Trainer trainer = trainerService.getByAuthToken(authToken);

        // 사용자 정보를 응답 객체로 변환
        TrainerResponse response = new TrainerResponse(trainer);

        return RestResponseUtil.ok("마이 페이지 조회 성공했습니다.", response);
    }

    /**
     * 회원을 저장하고 프로필을 저장합니다.
     *
     * @param request        가입 요청 객체
     * @param bindingResult  유효성 검사 결과 객체
     * @return ResponseEntity 객체
     * @throws IOException  파일 처리 예외 발생 시
     */
    @Transactional
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(@Valid JoinRequest request, BindingResult bindingResult) throws IOException {
        // 사용자 저장
        Trainer saveTrainer = trainerService.save(request);

        // 프로필 저장
        profileService.save(request.getProfile(), saveTrainer.getId());

        return RestResponseUtil.ok("회원 가입 성공 했습니다.", null);
    }

    /**
     * 사용자 정보를 업데이트합니다.
     *
     * @param request        정보 업데이트 요청 객체
     * @param bindingResult  유효성 검사 결과 객체
     * @return ResponseEntity 객체
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/")
    public ResponseEntity<?> update(@Valid UpdateInfoRequest request, BindingResult bindingResult) {
        // 정보 업데이트
        trainerService.updateInfo(request);

        return RestResponseUtil.ok("내 정보 수정 성공했습니다.", null);
    }

    /**
     * 비밀번호를 업데이트합니다.
     *
     * @param request        비밀번호 업데이트 요청 객체
     * @param bindingResult  유효성 검사 결과 객체
     * @return ResponseEntity 객체
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/password")
    public ResponseEntity<?> updatePassword(
            @Valid UpdatePasswordRequest request,
            BindingResult bindingResult) {
        // 비밀번호 업데이트
        trainerService.updatePassword(request);

        return RestResponseUtil.ok("비밀번호 변경 성공했습니다.", null);
    }

    /**
     * 회원 탈퇴를 처리합니다.
     *
     * @param request             HTTP 요청 객체
     * @param withdrawalRequest   회원 탈퇴 요청 객체
     * @param bindingResult       유효성 검사 결과 객체
     * @return ResponseEntity 객체
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/")
    public ResponseEntity<?> withdrawal(
            HttpServletRequest request,
            @Valid WithdrawalUserRequest withdrawalRequest,
            BindingResult bindingResult
    ) throws IOException {


        // 이메일을 기반으로 사용자 조회
        Trainer trainer = trainerService.getByEmail(withdrawalRequest.getEmail());

        // 프로필 조회
        Profile userProfile = profileService.getByTrainerId(trainer.getId());

        // 디렉토리 프로필 삭제
        profileService.deleteFileToDirectory(userProfile.getPath());

        // DB 프로필 삭제
        profileService.withdrawal(userProfile.getId());

        //사용자 삭제
        trainerService.withdrawal(trainer.getId());

        //토큰 삭제
        String authToken = tokenService.resolveAuthToken(request);
        tokenService.deleteToken(authToken);

        return RestResponseUtil
                .ok("회원 탈퇴 성공했습니다.", null);
    }

    /**
     * 로그인을 처리합니다.
     *
     * @param request         로그인 요청 객체
     * @param bindingResult   유효성 검사 결과 객체
     * @param response        HTTP 응답 객체
     * @return ResponseEntity 객체
     */
    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public ResponseEntity<?> login(@Valid LoginRequest request, BindingResult bindingResult, HttpServletResponse response) {
        // 사용자 로그인 처리
        Trainer trainer = trainerService.getByEmail(request.getEmail());
        trainerService.isPasswordMatches(request.getPassword(), trainer);

        // JWT 토큰 생성 및 설정
        String authToken = tokenService.setToken(request.getEmail(), response, role);

        // 로그인 응답 객체 생성
        LoginResponse loginResponse = LoginResponse
                .builder()
                .authToken(authToken)
                .role(role)
                .build();

        return RestResponseUtil.ok("로그인 성공 했습니다.", loginResponse);
    }
}

