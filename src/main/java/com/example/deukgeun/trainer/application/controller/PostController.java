package com.example.deukgeun.trainer.application.controller;

import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RequestMapping("/api/trainer/post")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final TrainerApplicationService trainerApplicationService;
    private final AuthTokenApplicationServiceImpl authTokenApplicationService;

    /**
     * 게시글을 삭제합니다.
     *
     * @param request HTTP 요청 객체입니다.
     * @param src     삭제할 게시글의 식별자(src)를 나타내는 매개변수입니다.
     * @return 게시글 삭제 결과를 나타내는 ResponseEntity 객체입니다.
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/")
    public ResponseEntity<?> deletePost(HttpServletRequest request, @RequestParam("src") String src) throws IOException {
        // HTTP 요청에서 인증 토큰을 추출합니다.
        String authToken = authTokenApplicationService.resolveAuthToken(request);

        // 추출한 인증 토큰을 사용하여 현재 로그인한 사용자의 이메일을 얻습니다.
        String email = authTokenApplicationService.getUserPk(authToken);

        // 사용자의 이메일과 게시글 식별자(src)를 이용하여 게시글을 삭제합니다.
        trainerApplicationService.deletePostByEmail(email);
        trainerApplicationService.deleteImageToS3(src);

        return RestResponseUtil.ok("게시글 삭제 성공했습니다.", null);
    }

    /**
     * Amazon S3에서 이미지를 삭제합니다.
     *
     * @param src 삭제할 이미지의 식별자(src)를 나타내는 매개변수입니다.
     * @throws IOException 이미지 삭제 과정에서 발생하는 입출력 예외를 처리합니다.
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/image")
    public void deleteS3Image(@RequestParam("src") String src) throws IOException {
        trainerApplicationService.deleteImageToS3(src);
    }

    /**
     * 사용자의 게시글을 업로드합니다.
     *
     * @param request HTTP 요청 객체입니다.
     * @param postRequest 업로드할 게시글 데이터를 포함하는 객체입니다.
     * @param bindingResult 유효성 검사 결과를 저장하는 객체입니다.
     * @return ResponseEntity 객체로 응답을 반환합니다.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> uploadPost(HttpServletRequest request, @Valid PostRequest postRequest, BindingResult bindingResult) {
        // 사용자의 인증 토큰을 해석하여 이메일을 추출합니다.
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);

        // 추출된 이메일로 게시글을 업로드하는 서비스 메서드를 호출합니다.
        trainerApplicationService.uploadPost(email, postRequest);

        return RestResponseUtil.ok("게시글 저장 성공했습니다.", null);
    }

    /**
     * 사용자가 업로드한 이미지를 Amazon S3에 저장합니다.
     *
     * @param request HTTP 요청 객체입니다.
     * @param response HTTP 응답 객체입니다.
     * @throws Exception 예외 발생 시 처리됩니다.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/image")
    public void uploadS3Image(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 사용자가 업로드한 이미지를 Amazon S3에 저장하고 결과 데이터를 받아옵니다.
        Map<Object, Object> responseData = trainerApplicationService.saveImageToS3(request, response);

        // 결과 데이터를 JSON 형식으로 변환합니다.
        String jsonResponseData = new Gson().toJson(responseData);

        // HTTP 응답 헤더와 내용을 설정하여 클라이언트에게 응답을 전송합니다.
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponseData);
    }
}
