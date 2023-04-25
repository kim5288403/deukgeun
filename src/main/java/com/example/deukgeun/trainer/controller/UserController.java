package com.example.deukgeun.trainer.controller;


import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.example.deukgeun.trainer.request.UserInfoUpdateRequest;
import com.example.deukgeun.trainer.response.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.deukgeun.commom.response.LoginResponse;
import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.request.LoginRequest;
import com.example.deukgeun.trainer.response.UserResponse.UserListResponse;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import lombok.RequiredArgsConstructor;


@RestController("trainer.controller.UserController")
@RequestMapping("/api/trainer")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final ValidateServiceImpl validateService;
    private final JwtServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;

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
        User user = userService.getUser(authToken);
        UserResponse response = new UserResponse(user);

        return RestResponseUtil.okResponse("마이 페이지 조회 성공했습니다.", response);
    }

    /**
     * 트레이너 이메일 가져오기
     * authToken 에서 email 추출하기
     *
     * @param request authToken 추출을 위한 파라미터
     * @return authToken 에서 추출된 email 데이터
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, path = "/email")
    public ResponseEntity<?> getEmail(HttpServletRequest request) throws Exception {
        String authToken = request.getHeader("Authorization").replace("Bearer ", "");
        String email = jwtService.getUserPk(authToken);

        return RestResponseUtil.okResponse("내 정보 비밀번호 조회 성공했습니다.", email);
    }

    /**
     * 트레이너 정보 수정
     *
     * @param request       from data 추출을 위한 파라미터
     * @param bindingResult from data validator error 결과를 담는 역할
     * @return 수정 완료 메세지
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/")
    public ResponseEntity<?> update(@Valid UserInfoUpdateRequest request, BindingResult bindingResult) {
        validateService.errorMessageHandling(bindingResult);
        userService.updateInfo(request);

        return RestResponseUtil.okResponse("내 정보 수정 성공했습니다.", null);
    }

    /**
     * 트레이너 회원 가입
     *
     * @param request
     * @param bindingResult
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> save(@Valid JoinRequest request, BindingResult bindingResult) {
        validateService.errorMessageHandling(bindingResult);
        User user = JoinRequest.create(request, passwordEncoder);
        userService.save(user);

        return RestResponseUtil.okResponse("회원 가입 성공 했습니다.", user);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public ResponseEntity<?> delete() {
        System.out.println("gd");
        return RestResponseUtil.okResponse("회원 탈퇴 성공 했습니다.", null);
    }

    /**
     * 트레이너 로그인
     *
     * @param request
     * @param bindingResult
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public ResponseEntity<?> login(@Valid LoginRequest request, BindingResult bindingResult, HttpServletResponse response) {
        validateService.errorMessageHandling(bindingResult);

        String authToken = jwtService.setCreateToken(request.getEmail(), response);

        LoginResponse loginResponse = LoginResponse.builder().authToken(authToken).role(role).build();

        return RestResponseUtil.okResponse("로그인 성공 했습니다.", loginResponse);
    }
}

