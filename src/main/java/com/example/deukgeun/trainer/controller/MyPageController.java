package com.example.deukgeun.trainer.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
  
  @RequestMapping(method = RequestMethod.POST, path = "/imageUpload")
  public void smarteditorImageUpload(HttpServletRequest request, HttpServletResponse response){
    try {
      //파일정보
      String sFileInfo = "";
      //파일명을 받는다 - 일반 원본파일명
      String sFilename = request.getHeader("file-name");
      //파일 확장자
      String sFilenameExt = sFilename.substring(sFilename.lastIndexOf(".")+1);
      //확장자를소문자로 변경
      sFilenameExt = sFilenameExt.toLowerCase();
          
      //이미지 검증 배열변수
      String[] allowFileArr = {"jpg","png","bmp","gif"};

      //확장자 체크
      int nCnt = 0;
      for(int i=0; i<allowFileArr.length; i++) {
          if(sFilenameExt.equals(allowFileArr[i])){
              nCnt++;
          }
      }
      
      //이미지가 아니라면
      if(nCnt == 0) {
          PrintWriter print = response.getWriter();
          print.print("NOTALLOW_"+sFilename);
          print.flush();
          print.close();
      } else {
          //디렉토리 설정 및 업로드 
          
          //파일경로
          String filePath = request.getServletContext().getRealPath("/img") + "\\";
          File file = new File(filePath);
          
          if(!file.exists()) {
              file.mkdirs();
          }
          
          String sRealFileNm = "";
          SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
          String today= formatter.format(new java.util.Date());
          sRealFileNm = today+UUID.randomUUID().toString() + sFilename.substring(sFilename.lastIndexOf("."));
          String rlFileNm = filePath + sRealFileNm;
          
          ///////////////// 서버에 파일쓰기 ///////////////// 
          InputStream inputStream = request.getInputStream();
          OutputStream outputStream=new FileOutputStream(rlFileNm);
          int numRead;
          byte bytes[] = new byte[Integer.parseInt(request.getHeader("file-size"))];
          while((numRead = inputStream.read(bytes,0,bytes.length)) != -1){
              outputStream.write(bytes,0,numRead);
          }
          if(inputStream != null) {
              inputStream.close();
          }
          outputStream.flush();
          outputStream.close();
          
          ///////////////// 이미지 /////////////////
          // 정보 출력
          sFileInfo += "&bNewLine=true";
          // img 태그의 title 속성을 원본파일명으로 적용시켜주기 위함
          sFileInfo += "&sFileName="+ sFilename;
          sFileInfo += "&sFileURL="+filePath+sRealFileNm;
          PrintWriter printWriter = response.getWriter();
          printWriter.print(sFileInfo);
          printWriter.flush();
          printWriter.close();
      }   
  } catch (Exception e) {
      e.printStackTrace();
  }
  }
  
  
}
