package com.example.deukgeun.trainer.controller;


import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.deukgeun.commom.enums.StatusEnum;
import com.example.deukgeun.commom.exception.RequestValidException;
import com.example.deukgeun.commom.response.Message;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.request.ProfileRequest;
import com.example.deukgeun.trainer.request.UserJoinRequest;
import com.example.deukgeun.trainer.response.UserListResponse;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;


@RestController("trainer.controller.UserController")
@RequestMapping("/trainer")
public class UserController {

  @Autowired
  private UserServiceImpl userService;

  @Autowired
  private ProfileServiceImpl profileService;

  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Autowired
  private ValidateServiceImpl validateService;

  // 트레이너 리스트 조건 검색
  @RequestMapping(method = RequestMethod.GET, path = "/")
  public ResponseEntity<?> list(String keyword) {
    Message response = null;
    
    try {
      List<UserListResponse> list = userService.getList(keyword);
      
      response = Message
          .builder()
          .data(list)
          .message("조회 성공 했습니다.")
          .code(StatusEnum.OK.getCode())
          .status(StatusEnum.OK.getStatus())
          .build();

      return ResponseEntity
          .ok()
          .body(response);
      
    } catch (Exception e) {
      return ResponseEntity
          .badRequest()
          .body(e.getMessage());
    }
  }

  // 트레이너 회원 가입
  @RequestMapping(method = RequestMethod.POST, path = "/join")
  public ResponseEntity<?> save(@RequestPart @Valid UserJoinRequest request,
      BindingResult bindingResult, @RequestPart(name = "profile", required = false) MultipartFile profile) {

    bindingResult = profileService.validator(profile, bindingResult);
    
    try {
      if (bindingResult.hasErrors()) {
        validateService.errorMessageHandling(bindingResult);
      }

      UUID uuid = UUID.randomUUID();
      ProfileRequest profileRequest = ProfileRequest
          .builder()
          .path(uuid.toString() + "_" + profile.getOriginalFilename())
          .build();
      Profile profileCreate = ProfileRequest.create(profileRequest);
      Long profileSaveId = profileService.save(profileCreate);
      profileService.serverSave(profile, profileRequest.getPath());
      
      User user = UserJoinRequest.create(request, passwordEncoder, profileSaveId);
      userService.save(user);
      
      Message response = Message
          .builder()
          .data(user)
          .message("회원 가입 성공 했습니다.")
          .code(StatusEnum.OK.getCode())
          .status(StatusEnum.OK.getStatus())
          .build();

      return ResponseEntity
          .ok()
          .body(response);
    } catch (RequestValidException e) {
      
      return ResponseEntity
          .badRequest()
          .body(e.getResponse());
    } catch (Exception e) {
      
      return ResponseEntity
          .badRequest()
          .body(e.getMessage());
    }
  }
  

}

