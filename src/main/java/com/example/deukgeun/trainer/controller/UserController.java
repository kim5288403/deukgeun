package com.example.deukgeun.trainer.controller;


import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.deukgeun.commom.enums.StatusEnum;
import com.example.deukgeun.commom.request.TokenRequest;
import com.example.deukgeun.commom.response.LoginResponse;
import com.example.deukgeun.commom.response.MessageResponse;
import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.global.provider.JwtProvider;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.request.LoginRequest;
import com.example.deukgeun.trainer.request.ProfileRequest;
import com.example.deukgeun.trainer.request.UserInfoUpdateRequest;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.response.UserListResponse;
import com.example.deukgeun.trainer.response.UserResponse;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import lombok.RequiredArgsConstructor;


@RestController("trainer.controller.UserController")
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class UserController {

  private final String role = "trainer";
  private final UserServiceImpl userService;
  private final ProfileServiceImpl profileService;
  private final ValidateServiceImpl validateService;
  private final JwtServiceImpl jwtService;
  private final JwtProvider jwtProvider;
  private final PasswordEncoder passwordEncoder;

  // 트레이너 리스트 조건 검색
  @RequestMapping(method = RequestMethod.GET, path = "/")
  public ResponseEntity<?> list(String keyword) {
    MessageResponse response = null;

    List<UserListResponse> list = userService.getList(keyword);

    response = MessageResponse
        .builder()
        .data(list)
        .message("조회 성공 했습니다.")
        .code(StatusEnum.OK.getCode())
        .status(StatusEnum.OK.getStatus())
        .build();

    return ResponseEntity
        .ok()
        .body(response);
  }

  // 트레이너 회원 가입
  @RequestMapping(method = RequestMethod.POST, path = "/join")
  public ResponseEntity<?> save(
      @RequestPart @Valid JoinRequest request,
      BindingResult bindingResult,
      @RequestPart(name = "profile", required = false) MultipartFile profile) {

    bindingResult = profileService.validator(profile, bindingResult);

    if (bindingResult.hasErrors()) {
      validateService.errorMessageHandling(bindingResult);
    }
    
    UUID uuid = UUID.randomUUID();
    String path = uuid.toString() + "_" + profile.getOriginalFilename();
    ProfileRequest profileRequest = ProfileRequest
        .builder()
        .path(path)
        .build();
    
    Profile profileCreate = ProfileRequest.create(profileRequest);
    Long profileSaveId = profileService.save(profileCreate);
    profileService.serverSave(profile, profileRequest.getPath());

    User user = JoinRequest.create(request, passwordEncoder, profileSaveId);
    userService.save(user);

    MessageResponse response = MessageResponse
        .builder()
        .data(user)
        .message("회원 가입 성공 했습니다.")
        .code(StatusEnum.OK.getCode())
        .status(StatusEnum.OK.getStatus())
        .build();

    return ResponseEntity
        .ok()
        .body(response);
  }

  @RequestMapping(method = RequestMethod.POST, path = "/login")
  public ResponseEntity<?> login(
      @Valid LoginRequest request,
      BindingResult bindingResult,
      HttpServletResponse response) {

    if (bindingResult.hasErrors()) {
      validateService.errorMessageHandling(bindingResult);
    }

    String email = request.getEmail();
    String authToken = jwtProvider.createAuthToken(email, role);
    String refreshToken = jwtProvider.createRefreshToken(email, role);
    
    jwtProvider.setHeaderAccessToken(response, authToken);
    jwtService.createToken(TokenRequest.create(authToken, refreshToken));

    LoginResponse loginResponse = LoginResponse.builder()
        .authToken(authToken)
        .role(role)
        .build();

    MessageResponse messageResponse = MessageResponse
        .builder()
        .data(loginResponse)
        .message("로그인 성공 했습니다.")
        .code(StatusEnum.OK.getCode())
        .status(StatusEnum.OK.getStatus())
        .build();

    return ResponseEntity
        .ok()
        .body(messageResponse);
  }
  
  @RequestMapping(method = RequestMethod.GET, path = "/my-page")
  public ResponseEntity<?> myPage(HttpServletRequest request) throws Exception{
    String authToken = request.getHeader("Authorization").replace("Bearer ", "");
    String email = jwtProvider.getUserPk(authToken);
    
    User user = userService.getUser(email);
    UserResponse userResponse = new UserResponse(user);
    
    MessageResponse messageResponse = MessageResponse
        .builder()
        .code(StatusEnum.OK.getCode())
        .status(StatusEnum.OK.getStatus())
        .data(userResponse)
        .message("마이 페이지 조회 성공했습니다.")
        .build();
    
    return ResponseEntity
        .ok()
        .body(messageResponse);
  }
  
  @RequestMapping(method = RequestMethod.POST, path = "/info/update")
  public ResponseEntity<?> infoUpdate(
      @RequestPart @Valid UserInfoUpdateRequest request,
      BindingResult bindingResult,
      @RequestPart(name = "profile", required = false) MultipartFile profile) {
    
    if (bindingResult.hasErrors()) {
      validateService.errorMessageHandling(bindingResult);
    }
    
    userService.infoUpdate(request);
    
    MessageResponse messageResponse = MessageResponse
        .builder()
        .code(StatusEnum.OK.getCode())
        .status(StatusEnum.OK.getStatus())
        .data(request)
        .message("내 정보 수정 성공했습니다.")
        .build();
    
    return ResponseEntity
        .ok()
        .body(messageResponse);
  }

}

