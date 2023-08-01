package com.example.deukgeun.trainer.application.controller;

import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@RequestMapping("/api/trainer/post")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final TrainerApplicationService trainerApplicationService;
    private final AuthTokenApplicationServiceImpl authTokenApplicationService;

    @RequestMapping(method = RequestMethod.DELETE, path = "/")
    public ResponseEntity<?> delete(HttpServletRequest request){
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        trainerApplicationService.deletePost(email);

        return RestResponseUtil.ok("게시글 삭제 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/image")
    public void deleteServerImage(@RequestParam("src") String src) throws IOException {
        trainerApplicationService.deleteImageToServer(src);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/image/*")
    public void getServerImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = trainerApplicationService.getServerImage(request.getRequestURI());
        response.setHeader("Content-Type", request.getServletContext().getMimeType(file.getName()));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getPostById(@PathVariable("id") Long id) {
        Trainer trainer = trainerApplicationService.findById(id);
        Post post = trainer.getPost();

        return RestResponseUtil
                .ok("조회 성공 했습니다.", post);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> uploadPost(HttpServletRequest request, @Valid PostRequest postRequest, BindingResult bindingResult) throws Exception {
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        trainerApplicationService.uploadPost(email, postRequest);

        return RestResponseUtil.ok("게시글 저장 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/image")
    public void uploadServerImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<Object, Object> responseData = trainerApplicationService.saveImageToServer(request, response);
        String jsonResponseData = new Gson().toJson(responseData);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponseData);
    }
}
