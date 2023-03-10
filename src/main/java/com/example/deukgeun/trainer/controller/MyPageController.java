package com.example.deukgeun.trainer.controller;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.example.deukgeun.trainer.request.PasswordUpdateRequest;
import com.example.deukgeun.trainer.request.UserInfoUpdateRequest;
import com.example.deukgeun.trainer.request.WithdrawalRequest;
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
  private final PasswordEncoder passwordEncoder;
  
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
        .message("?????? ????????? ?????? ??????????????????.")
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
        .message("??? ?????? ?????? ??????????????????.")
        .build();
    
    return ResponseEntity
        .ok()
        .body(messageResponse);
  }
  
  @RequestMapping(method = RequestMethod.GET, path = "/profile")
  public ResponseEntity<?> getProfile(HttpServletRequest request) throws Exception {
    String authToken = request.getHeader("Authorization").replace("Bearer ", "");
    String email = jwtProvider.getUserPk(authToken);
    Long profileId = userService.getProfileId(email);
    
    Profile profile = profileService.getProfile(profileId);
    ProfileResponse profileResponse = ProfileResponse.builder()
    .path(profile.getPath())
    .build();
    
    MessageResponse messageResponse = MessageResponse
        .builder()
        .code(StatusEnum.OK.getCode())
        .status(StatusEnum.OK.getStatus())
        .data(profileResponse)
        .message("????????? ????????? ?????? ??????????????????.")
        .build();
    
    return ResponseEntity
        .ok()
        .body(messageResponse);
  }

  @RequestMapping(method = RequestMethod.POST, path = "/profile/update")
  public ResponseEntity<?> updateProfile(HttpServletRequest request, MultipartFile profile) throws Exception {
    String authToken = request.getHeader("Authorization").replace("Bearer ", "");
    String email = jwtProvider.getUserPk(authToken);
    Long profileId = userService.getProfileId(email);
    
    if (profile != null) {
      UUID uuid = UUID.randomUUID();
      String path = uuid.toString() + "_" + profile.getOriginalFilename();
      
      //DB ????????????
      profileService.updateProfile(profileId, path);
      //server ??????
      profileService.saveServer(profile, path);
      
      //server ????????? ?????? ??????
      Profile userProfile = profileService.getProfile(profileId);
      profileService.deleteServer(userProfile.getPath());
    }
    
    MessageResponse messageResponse = MessageResponse
        .builder()
        .code(StatusEnum.OK.getCode())
        .status(StatusEnum.OK.getStatus())
        .data(null)
        .message("??? ?????? ?????? ??????????????????.")
        .build();
    
    return ResponseEntity
        .ok()
        .body(messageResponse);
  }
  
  @RequestMapping(method = RequestMethod.GET, path = "/password")
  public ResponseEntity<?> getPassword(HttpServletRequest request) throws Exception {
    String authToken = request.getHeader("Authorization").replace("Bearer ", "");
    String email = jwtProvider.getUserPk(authToken);
    
    MessageResponse messageResponse = MessageResponse
        .builder()
        .code(StatusEnum.OK.getCode())
        .status(StatusEnum.OK.getStatus())
        .data(email)
        .message("??? ?????? ???????????? ?????? ??????????????????.")
        .build();
    
    return ResponseEntity
        .ok()
        .body(messageResponse);
  }
  
  @RequestMapping(method = RequestMethod.POST, path = "/password/update")
  public ResponseEntity<?> updatePassword(
      @Valid PasswordUpdateRequest request,
      BindingResult bindingResult){
    
    if (bindingResult.hasErrors()) {
      validateService.errorMessageHandling(bindingResult);
    }
    
    String email = request.getEmail();
    String password = passwordEncoder.encode(request.getNewPassword());
    userService.updatePassword(email, password);
    
    MessageResponse messageResponse = MessageResponse
        .builder()
        .code(StatusEnum.OK.getCode())
        .status(StatusEnum.OK.getStatus())
        .data(null)
        .message("???????????? ?????? ??????????????????.")
        .build();
    
    return ResponseEntity
        .ok()
        .body(messageResponse);
  }
  
  @RequestMapping(method = RequestMethod.POST, path = "/withdrawal")
  public ResponseEntity<?> withdrawal(
      HttpServletRequest request,
      @Valid WithdrawalRequest withdrawalRequest,
      BindingResult bindingResult
      ) throws Exception {
    
    if (bindingResult.hasErrors()) {
      validateService.errorMessageHandling(bindingResult);
    }
    
    String authToken = request.getHeader("Authorization").replace("Bearer ", "");
    String email = withdrawalRequest.getEmail();
    User user = userService.getUser(email);
    
    
    Long profileId = user.getProfileId();
    Profile userProfile = profileService.getProfile(profileId);
    
    //????????? ??????
    userService.withdrawal(user);
    
    //????????? ????????? ??????
    profileService.deleteServer(userProfile.getPath());
    profileService.withdrawal(profileId);
    
    //?????? ??????
    jwtProvider.deleteTokenEntity(authToken);
    
    MessageResponse messageResponse = MessageResponse
        .builder()
        .code(StatusEnum.OK.getCode())
        .status(StatusEnum.OK.getStatus())
        .data(null)
        .message("?????? ?????? ??????????????????.")
        .build();
    
    return ResponseEntity
        .ok()
        .body(messageResponse);
  }
  
}
