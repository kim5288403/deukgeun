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

    @RequestMapping(method = RequestMethod.DELETE, path = "/")
    public ResponseEntity<?> deletePost(HttpServletRequest request, @RequestParam("src") String src){
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        trainerApplicationService.deletePost(email, src);

        return RestResponseUtil.ok("게시글 삭제 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/image")
    public void deleteS3Image(@RequestParam("src") String src) throws IOException {
        trainerApplicationService.deleteImageToS3(src);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> uploadPost(HttpServletRequest request, @Valid PostRequest postRequest, BindingResult bindingResult) throws Exception {
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        trainerApplicationService.uploadPost(email, postRequest);

        return RestResponseUtil.ok("게시글 저장 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/image")
    public void uploadS3Image(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<Object, Object> responseData = trainerApplicationService.saveImageToS3(request, response);
        String jsonResponseData = new Gson().toJson(responseData);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponseData);
    }
}
