package com.example.deukgeun.trainer.controller;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
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
import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.global.provider.JwtProvider;
import com.example.deukgeun.trainer.entity.Post;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.request.PasswordUpdateRequest;
import com.example.deukgeun.trainer.request.PostRequest;
import com.example.deukgeun.trainer.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.request.UserInfoUpdateRequest;
import com.example.deukgeun.trainer.request.WithdrawalRequest;
import com.example.deukgeun.trainer.response.LicenseListResponse;
import com.example.deukgeun.trainer.response.PostResponse;
import com.example.deukgeun.trainer.response.ProfileResponse;
import com.example.deukgeun.trainer.response.ProfileResponse.ProfileAndUserResponse;
import com.example.deukgeun.trainer.response.UserResponse;
import com.example.deukgeun.trainer.service.implement.LicenseServiceImpl;
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
    private final JwtServiceImpl jwtService;
    private final LicenseServiceImpl licenseService;



    @RequestMapping(method = RequestMethod.GET, path = "/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) throws Exception {

        String authToken = request.getHeader("Authorization").replace("Bearer ", "");
        Long profileId = profileService.getProfileId(authToken);
        Profile profile = profileService.getProfile(profileId);
        ProfileResponse profileResponse = new ProfileResponse(profile);

        return RestResponseUtil
                .okResponse("프로필 이미지 조회 성공했습니다.", profileResponse);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/profile/update")
    public ResponseEntity<?> updateProfile(HttpServletRequest request, MultipartFile profile) throws Exception {

        String authToken = request.getHeader("Authorization").replace("Bearer ", "");
        Long profileId = profileService.getProfileId(authToken);

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

        return RestResponseUtil
                .okResponse("내 정보 수정 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/email")
    public ResponseEntity<?> getEmail(HttpServletRequest request) throws Exception {
        String authToken = request.getHeader("Authorization").replace("Bearer ", "");
        String email = jwtProvider.getUserPk(authToken);

        return RestResponseUtil
                .okResponse("내 정보 비밀번호 조회 성공했습니다.", email);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/password/update")
    public ResponseEntity<?> updatePassword(
            @Valid PasswordUpdateRequest request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            validateService.errorMessageHandling(bindingResult);
        }

        String email = request.getEmail();
        String password = passwordEncoder.encode(request.getNewPassword());
        userService.updatePassword(email, password);

        return RestResponseUtil
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

        Long profileId = profileService.getProfileId(authToken);

        Profile userProfile = profileService.getProfile(profileId);

        //사용자 삭제
        userService.withdrawal(authToken);

        //포로필 이미지 삭제
        profileService.deleteServer(userProfile.getPath());
        profileService.withdrawal(profileId);

        //토큰 삭제
        jwtService.deleteToken(authToken);

        return RestResponseUtil
                .okResponse("회원 탈퇴 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/post")
    public ResponseEntity<?> getPost(HttpServletRequest request) throws Exception {

        String authToken = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = userService.getUserId(authToken);
        Post post = postService.findByUserId(userId);
        PostResponse response = new PostResponse(post);

        return RestResponseUtil
                .okResponse("마이 페이지 조회 성공했습니다.", response);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/post/profile")
    public ResponseEntity<?> getProfileAndUser(HttpServletRequest request) throws Exception {

        String authToken = request.getHeader("Authorization").replace("Bearer ", "");
        Long profileId = profileService.getProfileId(authToken);
        Profile profile = profileService.getProfile(profileId);
        ProfileAndUserResponse response = new ProfileResponse.ProfileAndUserResponse(profile);

        return RestResponseUtil
                .okResponse("마이 페이지 조회 성공했습니다.", response);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/post/upload")
    public ResponseEntity<?> uploadPost(HttpServletRequest request,
                                        @Valid PostRequest postRequest,
                                        BindingResult bindingResult
    ) throws Exception {

        if (bindingResult.hasErrors()) {
            validateService.errorMessageHandling(bindingResult);
        }

        String authToken = request.getHeader("Authorization").replace("Bearer ", "");
        postService.uploadPost(postRequest, authToken);

        return RestResponseUtil
                .okResponse("게시글 저장 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/post/uploadImage")
    public void uploadPostImage(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<Object, Object> responseData = postService.saveImage(request, response);
        String jsonResponseData = new Gson().toJson(responseData);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponseData);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/post/*")
    public void getPostImage(HttpServletRequest request, HttpServletResponse response) throws Exception {

        File file = postService.getServerImage(request.getRequestURI());
        response.setHeader("Content-Type", request.getServletContext().getMimeType(file.getName()));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/post/remove")
    public void removePostImage(HttpServletRequest request) throws Exception {

        String src = request.getParameter("src");
        postService.deletePostImage(src);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/license")
    public ResponseEntity<?> getLicense(HttpServletRequest request) throws Exception {

        String authToken = request.getHeader("Authorization").replace("Bearer ", "");
        List<LicenseListResponse> response = licenseService.getLicense(authToken);

        return RestResponseUtil
                .okResponse("자격증 조회 성공했습니다.", response);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/license")
    public ResponseEntity<?> saveLicense(HttpServletRequest request, @Valid SaveLicenseRequest saveLicenseRequest, BindingResult bindingResult) throws Exception {

        String authToken = request.getHeader("Authorization").replace("Bearer ", "");
        if (bindingResult.hasErrors()) {
            validateService.errorMessageHandling(bindingResult);
        }

        licenseService.saveLicense(saveLicenseRequest, authToken);

        return RestResponseUtil
                .okResponse("자격증 등록 성공했습니다.", null);
    }

}
