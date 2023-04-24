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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.deukgeun.commom.response.LoginResponse;
import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.entity.Post;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.request.LoginRequest;
import com.example.deukgeun.trainer.response.PostResponse;
import com.example.deukgeun.trainer.response.UserResponse.UserListResponse;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.service.implement.PostServiceImpl;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import lombok.RequiredArgsConstructor;


@RestController("trainer.controller.UserController")
@RequestMapping("/api/trainer")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final ProfileServiceImpl profileService;
    private final ValidateServiceImpl validateService;
    private final JwtServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;
    private final PostServiceImpl postService;

    @Value("${deukgeun.role.trainer}")
    private String role;

    /**
     * 트레이너 조건 검색
     * @param keyword
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> getList(String keyword) {
        List<UserListResponse> list = userService.getList(keyword);

        return RestResponseUtil
                .okResponse("조회 성공 했습니다.", list);
    }

    /**
     * 트레이너 상세보기
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getDetail(@PathVariable("id") Long id) throws Exception {
        Post post = postService.findByUserId(id);
        PostResponse response = new PostResponse(post);

        return RestResponseUtil
                .okResponse("조회 성공 했습니다.", response);
    }

    /**
     * 트레이너 기본 정보
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, path = "/info")
    public ResponseEntity<?> getInfo(HttpServletRequest request) throws Exception {

        String authToken = request.getHeader("Authorization").replace("Bearer ", "");
        User user = userService.getUser(authToken);
        UserResponse response = new UserResponse(user);

        return RestResponseUtil
                .okResponse("마이 페이지 조회 성공했습니다.", response);
    }

    /**
     * 트레이너 기본 정보 수정
     * @param request
     * @param bindingResult
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/info")
    public ResponseEntity<?> UpdateInfo(
            @Valid UserInfoUpdateRequest request,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            validateService.errorMessageHandling(bindingResult);
        }

        userService.updateInfo(request);

        return RestResponseUtil
                .okResponse("내 정보 수정 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/join")
    public ResponseEntity<?> join(
            @RequestPart @Valid JoinRequest request,
            BindingResult bindingResult,
            @RequestPart(name = "profile", required = false) MultipartFile profile) {
        bindingResult = profileService.validator(profile, bindingResult);

        if (bindingResult.hasErrors()) {
            validateService.errorMessageHandling(bindingResult);
        }

        profileService.save(profile);

        User user = JoinRequest.create(request, passwordEncoder);
        userService.save(user);

        return RestResponseUtil
                .okResponse("회원 가입 성공 했습니다.", user);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public ResponseEntity<?> login(
            @Valid LoginRequest request,
            BindingResult bindingResult,
            HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            validateService.errorMessageHandling(bindingResult);
        }

        String authToken = jwtService.setCreateToken(request.getEmail(), response);

        LoginResponse loginResponse = LoginResponse.builder()
                .authToken(authToken)
                .role(role)
                .build();

        return RestResponseUtil
                .okResponse("로그인 성공 했습니다.", loginResponse);
    }
}

