package com.example.deukgeun.trainer.controller;


import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.request.*;
import com.example.deukgeun.trainer.response.UserResponse;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.deukgeun.commom.response.LoginResponse;
import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.response.UserResponse.UserListResponse;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import lombok.RequiredArgsConstructor;


@RestController("trainer.controller.UserController")
@RequestMapping("/api/trainer")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final ValidateServiceImpl validateService;
    private final JwtServiceImpl jwtService;
    private final ProfileServiceImpl profileService;

    @Value("${deukgeun.role.trainer}")
    private String role;

    /**
     * 트레이너 조건 검색
     *
     * @param keyword 검색어
     * @return 검색된 트레이너 리스트 반환
     */
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> getList(String keyword) {
        List<UserListResponse> list = userService.getList(keyword);

        return RestResponseUtil.okResponse("조회 성공 했습니다.", list);
    }

    /**
     * 트레이너 상세보기
     * authToken 에서 추출된 email 에 해당된 트레이너 데이터 가져오기
     *
     * @param request authToken 추출을 위한 파라미터
     * @return email 에 해당된 트레이너 데이터
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, path = "/detail")
    public ResponseEntity<?> getDetail(HttpServletRequest request) throws Exception {
        String authToken = request.getHeader("Authorization").replace("Bearer ", "");
        User user = userService.getUserByAuthToken(authToken);
        UserResponse response = new UserResponse(user);

        return RestResponseUtil.okResponse("마이 페이지 조회 성공했습니다.", response);
    }

    /**
     * 트레이너 이메일 가져오기
     * authToken 에서 email 추출하기
     *
     * @param request authToken 추출을 위한 파라미터
     * @return authToken 에서 추출된 email 데이터
     */
    @RequestMapping(method = RequestMethod.GET, path = "/email")
    public ResponseEntity<?> getEmail(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization").replace("Bearer ", "");
        String email = jwtService.getUserPk(authToken);

        return RestResponseUtil.okResponse("내 정보 비밀번호 조회 성공했습니다.", email);
    }

    /**
     * 트레이너 회원 가입
     * 사용자를 저장 한 후 사용자 save get id 로 프로필 이미지 저장
     *
     * @param request 유효성 검사를 통과한 회원가입 데이터
     * @param bindingResult from data validator 결과를 담는 역할
     * @return RestResponseUtil
     */
    @Transactional
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(@Valid JoinRequest request, BindingResult bindingResult) {
        validateService.errorMessageHandling(bindingResult);

        //user save
        Long userId = userService.save(request);
        //profile save
        profileService.save(request.getProfile(), userId);

        return RestResponseUtil.okResponse("회원 가입 성공 했습니다.", null);
    }

    /**
     * 트레이너 정보 수정
     *
     * @param request       from update data 추출을 위한 파라미터
     * @param bindingResult from data validator 결과를 담는 역할
     * @return RestResponseUtil
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/")
    public ResponseEntity<?> update(@Valid UserUpdateRequest request, BindingResult bindingResult) {
        validateService.errorMessageHandling(bindingResult);
        userService.updateInfo(request);

        return RestResponseUtil.okResponse("내 정보 수정 성공했습니다.", null);
    }

    /**
     * 트레이너 비밀번호 수정
     * 기존 비밀번호, 변경 비밀번호 유효성 검사 후 비밀번호 변경
     *
     * @param request  from update data 추출을 위한 파라미터
     * @param bindingResult from data validator 결과를 담는 역할
     * @return RestResponseUtil
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/password")
    public ResponseEntity<?> updatePassword(
            @Valid PasswordUpdateRequest request,
            BindingResult bindingResult) {

        validateService.errorMessageHandling(bindingResult);
        userService.updatePassword(request);

        return RestResponseUtil
                .okResponse("비밀번호 변경 성공했습니다.", null);
    }

    /**
     * 트레이너 회원 탈퇴
     * 트레이너 탈퇴 유효성 검사후 사용자 관련된 데이터 삭제 및 회원 탈퇴
     *
     * @param request authToken 추출을 위한 파라미터
     * @param withdrawalRequest from withdrawal data 추출을 위한 파라미터
     * @param bindingResult from data validator 결과를 담는 역할
     * @return RestResponseUtil
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/")
    public ResponseEntity<?> withdrawal(
            HttpServletRequest request,
            @Valid WithdrawalRequest withdrawalRequest,
            BindingResult bindingResult
    ) throws Exception {

        validateService.errorMessageHandling(bindingResult);

        String authToken = request.getHeader("Authorization").replace("Bearer ", "");
        Long profileId = profileService.getProfileId(authToken);
        Profile userProfile = profileService.getProfile(profileId);

        //포로필 이미지 삭제
        profileService.deleteServer(userProfile.getPath());

        profileService.withdrawal(profileId);

        //사용자 삭제
        userService.withdrawal(authToken);

        //토큰 삭제
        jwtService.deleteToken(authToken);

        return RestResponseUtil
                .okResponse("회원 탈퇴 성공했습니다.", null);
    }

    /**
     * 트레이너 로그인
     * 로그인 데이터로 authToken 생성, 저장, 반환
     *
     * @param request 유효성 검사를 통과한 로그인 데이터
     * @param bindingResult  from data validator 결과를 담는 역할
     * @param response
     * @return LoginResponse => authToken, role
     */
    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public ResponseEntity<?> login(@Valid LoginRequest request, BindingResult bindingResult, HttpServletResponse response) {
        validateService.errorMessageHandling(bindingResult);

        String authToken = jwtService.setCreateToken(request.getEmail(), response);

        LoginResponse loginResponse = LoginResponse.builder().authToken(authToken).role(role).build();

        return RestResponseUtil.okResponse("로그인 성공 했습니다.", loginResponse);
    }
}

