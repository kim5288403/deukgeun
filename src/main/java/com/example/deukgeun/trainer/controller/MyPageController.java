package com.example.deukgeun.trainer.controller;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.deukgeun.commom.enums.StatusEnum;
import com.example.deukgeun.commom.response.MessageResponse;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.global.provider.JwtProvider;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.request.UserInfoUpdateRequest;
import com.example.deukgeun.trainer.response.ProfileResponse;
import com.example.deukgeun.trainer.response.UserResponse;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import lombok.RequiredArgsConstructor;

@RestController("trainer.controller.MyPageController")
@RequestMapping("/api/trainer/my-page")
@RequiredArgsConstructor
public class MyPageController {
  
  private final JwtProvider jwtProvider;
  private final UserServiceImpl userService;
  private final ValidateServiceImpl validateService;
  private final ProfileServiceImpl profileService;
  
  @RequestMapping(method = RequestMethod.GET, path = "/info")
  public ResponseEntity<?> getInfo(HttpServletRequest request) throws Exception{
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
  public ResponseEntity<?> UpdateInfo(
      @Valid UserInfoUpdateRequest request,
      BindingResult bindingResult
      ) {
    
    if (bindingResult.hasErrors()) {
      validateService.errorMessageHandling(bindingResult);
    }
    
    userService.updateInfo(request);
    
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
  
  @RequestMapping(method = RequestMethod.GET, path = "/profile")
  public ResponseEntity<?> getProfile(HttpServletRequest request) throws Exception {
    String authToken = request.getHeader("Authorization").replace("Bearer ", "");
    Long profileId = getProfileId(authToken);
    
    Profile profile = profileService.getProfile(profileId);
    ProfileResponse profileResponse = ProfileResponse.builder()
    .path(profile.getPath())
    .build();
    
    MessageResponse messageResponse = MessageResponse
        .builder()
        .code(StatusEnum.OK.getCode())
        .status(StatusEnum.OK.getStatus())
        .data(profileResponse)
        .message("프로필 이미지 조회 성공했습니다.")
        .build();
    
    return ResponseEntity
        .ok()
        .body(messageResponse);
  }

  @RequestMapping(method = RequestMethod.POST, path = "/profile/update")
  public ResponseEntity<?> UpdateProfile(HttpServletRequest request, MultipartFile profile) throws Exception {
    String authToken = request.getHeader("Authorization").replace("Bearer ", "");
    Long profileId = getProfileId(authToken);
    
    if (profile != null) {
      UUID uuid = UUID.randomUUID();
      String path = uuid.toString() + "_" + profile.getOriginalFilename();
      
      //DB 업데이트
      profileService.updateProfile(profileId, path);
      //server 저장
      profileService.saveServer(profile, path);
      
      //server 저장된 파일 삭제
      Profile userProfile = profileService.getProfile(profileId);
      profileService.deleteServer(userProfile.getPath());
    }
    
    MessageResponse messageResponse = MessageResponse
        .builder()
        .code(StatusEnum.OK.getCode())
        .status(StatusEnum.OK.getStatus())
        .data(profile)
        .message("내 정보 수정 성공했습니다.")
        .build();
    
    return ResponseEntity
        .ok()
        .body(messageResponse);
  }
  
  public Long getProfileId(String authToken) throws Exception {
    String email = jwtProvider.getUserPk(authToken);
    User user = userService.getUser(email);
    return user.getProfileId();
  }
  
}
