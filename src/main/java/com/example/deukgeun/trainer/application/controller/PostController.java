package com.example.deukgeun.trainer.application.controller;

import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.application.dto.response.PostResponse;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.example.deukgeun.trainer.application.service.implement.PostApplicationServiceImpl;
import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.util.Map;

@RequestMapping("/api/trainer/post")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostApplicationServiceImpl postApplicationService;
    private final TrainerApplicationService trainerApplicationService;
    private final AuthTokenApplicationServiceImpl authTokenApplicationService;

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        postApplicationService.deleteById(id);

        return RestResponseUtil.ok("게시글 삭제 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/")
    public void deleteServerImage(@RequestParam("src") String src) {
        String path = postApplicationService.getFilePathFromUrl(src);
        File file = new File(path);
        postApplicationService.deleteFileToDirectory(file);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/image/*")
    public void getServerImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = postApplicationService.getServerImage(request.getRequestURI());
        response.setHeader("Content-Type", request.getServletContext().getMimeType(file.getName()));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getDetailByUserId(@PathVariable("id") Long id) {
        Post post = postApplicationService.findByTrainerId(id);
        PostResponse response = new PostResponse(post);

        return RestResponseUtil
                .ok("조회 성공 했습니다.", response);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> getDetailByAuthToken(HttpServletRequest request) {
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        Long trainerId = trainerApplicationService.findByEmail(email).getId();
        Post post = postApplicationService.findByTrainerId(trainerId);

        PostResponse response = new PostResponse(post);

        return RestResponseUtil
                .ok("조회 성공 했습니다.", response);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> upload(HttpServletRequest request, @Valid PostRequest postRequest, BindingResult bindingResult) throws Exception {
        String authToken = authTokenApplicationService.resolveAuthToken(request);
        String email = authTokenApplicationService.getUserPk(authToken);
        Trainer trainer = trainerApplicationService.findByEmail(email);
        Long trainerId = trainer.getId();
        postApplicationService.upload(postRequest, trainerId);

        return RestResponseUtil.ok("게시글 저장 성공했습니다.", null);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/image")
    public void uploadServerImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<Object, Object> responseData = postApplicationService.saveImage(request, response);
        String jsonResponseData = new Gson().toJson(responseData);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponseData);
    }
}
