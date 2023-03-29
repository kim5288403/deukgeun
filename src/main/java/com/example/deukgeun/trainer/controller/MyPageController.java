package com.example.deukgeun.trainer.controller;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.validation.Valid;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.deukgeun.commom.response.RestResponseUtil;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.global.provider.JwtProvider;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.request.PasswordUpdateRequest;
import com.example.deukgeun.trainer.request.PostRequest;
import com.example.deukgeun.trainer.request.UserInfoUpdateRequest;
import com.example.deukgeun.trainer.request.WithdrawalRequest;
import com.example.deukgeun.trainer.response.ProfileResponse;
import com.example.deukgeun.trainer.response.UserResponse;
import com.example.deukgeun.trainer.service.implement.PostServiceImpl;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import com.google.gson.Gson;
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
  private final PostServiceImpl postService;
  
  @Value("${trainer.post.filePath}")
  private String postFilePath;
  @Value("${trainer.post.url}")
  private String postUrl;
  
  @RequestMapping(method = RequestMethod.GET, path = "/info")
  public ResponseEntity<?> getInfo(HttpServletRequest request) throws Exception{
    String authToken = request.getHeader("Authorization").replace("Bearer ", "");
    String email = jwtProvider.getUserPk(authToken);
    
    User user = userService.getUser(email);
    UserResponse userResponse = new UserResponse(user);
    
    return new RestResponseUtil()
        .okResponse("마이 페이지 조회 성공했습니다.", userResponse);
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
    
    return new RestResponseUtil()
        .okResponse("내 정보 수정 성공했습니다.", null);
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
    
    return new RestResponseUtil()
        .okResponse("프로필 이미지 조회 성공했습니다.", profileResponse);
  }

  @RequestMapping(method = RequestMethod.POST, path = "/profile/update")
  public ResponseEntity<?> updateProfile(HttpServletRequest request, MultipartFile profile) throws Exception {
    String authToken = request.getHeader("Authorization").replace("Bearer ", "");
    String email = jwtProvider.getUserPk(authToken);
    Long profileId = userService.getProfileId(email);
    
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
    
    return new RestResponseUtil()
        .okResponse("내 정보 수정 성공했습니다.", null);
  }
  
  @RequestMapping(method = RequestMethod.GET, path = "/email")
  public ResponseEntity<?> getEmail(HttpServletRequest request) throws Exception {
    String authToken = request.getHeader("Authorization").replace("Bearer ", "");
    String email = jwtProvider.getUserPk(authToken);
    
    return new RestResponseUtil()
        .okResponse("내 정보 비밀번호 조회 성공했습니다.", email);
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
    
    return new RestResponseUtil()
        .okResponse("비밀번호 변경 성공했습니다.", null);
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
    
    //사용자 삭제
    userService.withdrawal(user);
    
    //포로필 이미지 삭제
    profileService.deleteServer(userProfile.getPath());
    profileService.withdrawal(profileId);
    
    //토큰 삭제
    jwtProvider.deleteTokenEntity(authToken);
    
    return new RestResponseUtil()
    .okResponse("회원 탈퇴 성공했습니다.", null);
  }
  
  @RequestMapping(method = RequestMethod.POST, path = "/post/upload")
  public ResponseEntity<?> postUpload(PostRequest request, HttpServletResponse response){
    
    postService.save(request);
    
    return new RestResponseUtil()
        .okResponse("게시글 저장 성공했습니다.", null);
  }
  
  @RequestMapping(method = RequestMethod.POST, path = "/post/uploadImage")
  public void postUploadImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
    
    File uploads = new File(postFilePath);
    String multipartContentType = "multipart/form-data";
    String fieldname = "file";
    Part filePart = request.getPart(fieldname);
    Map< Object, Object > responseData = null;
    
    final PrintWriter writer = response.getWriter();
    
    String linkName = null;
    String name = null;
    
    if (request.getContentType() == null ||
        request.getContentType().toLowerCase().indexOf(multipartContentType) == -1) {

        throw new Exception("Invalid contentType. It must be " + multipartContentType);
    }
    
    String type = filePart.getContentType();
    type = type.substring(type.lastIndexOf("/") + 1);
    
    String extension = type;
    extension = (extension != null && extension != "") ? "." + extension : extension;
    name = UUID.randomUUID().toString() + extension ;
    
    linkName = postUrl + name;
    
    String mimeType = filePart.getContentType();
    String[] allowedMimeTypes = new String[] {
        "image/gif",
        "image/jpeg",
        "image/pjpeg",
        "image/x-png",
        "image/png",
        "image/svg+xml"
    };
    
    if (!ArrayUtils.contains(allowedMimeTypes, mimeType.toLowerCase())) {

        // Delete the uploaded image if it dose not meet the validation.
        File file = new File(uploads + name);
        if (file.exists()) {
            file.delete();
        }

        throw new Exception("Image does not meet the validation.");
    }
    
    File file = new File(uploads, name);
    
    try (InputStream input = filePart.getInputStream()) {
        Files.copy(input, file.toPath());
    } catch (Exception e) {
      writer.println("<br/> ERROR: " + e);
    }
    
    responseData = new HashMap< Object, Object > ();
    responseData.put("link", linkName);
    
    // Send response data.
    String jsonResponseData = new Gson().toJson(responseData);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(jsonResponseData);
  }
  
  
  @RequestMapping(method = RequestMethod.GET, path = "/post/*")
  public void postImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String[] url = request.getRequestURI().split("/");
    String filename = url[url.length-1];
    File file = new File(postFilePath, filename);

    response.setHeader("Content-Type", request.getServletContext().getMimeType(filename));
    response.setHeader("Content-Length", String.valueOf(file.length()));
    response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
    Files.copy(file.toPath(), response.getOutputStream());
  }
  
}
