package com.example.deukgeun.trainer.controller;


import java.util.List;
import java.util.Map;
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
import com.example.deukgeun.commom.response.Message;
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
  
  //트레이너 리스트 조건 검색
  @RequestMapping(method = RequestMethod.GET, path = "/")
  public ResponseEntity<?> list(String keyword) {
    try {
      List<UserListResponse> list = userService.getList(keyword);
      
      return ResponseEntity.ok().body(
          Message.builder().
          data(list).
          message("트레이너 조회 성공 했습니다.").
          code(StatusEnum.OK.getCode()).
          status(StatusEnum.OK.getStatus()).
          build());

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Message.builder().code(StatusEnum.BAD_REQUEST.getCode()).data(keyword)
          .message(e.getMessage()).status(StatusEnum.BAD_REQUEST.getStatus()).build());
    }
  }
  
  //트레이너 회원 가입
  @RequestMapping(method = RequestMethod.POST, path = "/join")
  public ResponseEntity<?> save(@RequestPart @Valid UserJoinRequest request,
      BindingResult bindingResult, @RequestPart MultipartFile profile) {
    
    bindingResult = profileService.validator(profile, bindingResult);
    
    if (bindingResult.hasErrors()) {
      /* 유효성 통과 못한 필드와 메시지를 핸들링 */
      Map<String, String> validatorResult = userService.validateHandling(bindingResult);

      return ResponseEntity.ok().body(Message.builder().data(validatorResult)
          .message("회원 가입 실패 했습니다.").status(StatusEnum.BAD_REQUEST.getStatus()).build());
    }

    try {
      userService.checkEmailDuplication(request);
      User user = UserJoinRequest.create(request, passwordEncoder);
      Long userId = userService.save(user);
      
      UUID uuid = UUID.randomUUID();
      ProfileRequest profileRequest = ProfileRequest.builder().trainerUserId(userId)
          .path(uuid.toString() + "_" + profile.getOriginalFilename()).build();

      Profile profileCreate = ProfileRequest.create(profileRequest);
      profileService.save(profileCreate);
      profileService.serverSave(profile, profileRequest.getPath());

      return ResponseEntity.ok().body(
          Message.builder().data(user).message("회원 가입 성공 했습니다.").status(StatusEnum.OK.getStatus()).build());

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Message.builder().data(request)
          .message(e.getMessage()).status(StatusEnum.BAD_REQUEST.getStatus()).build());
    }
  }

}

