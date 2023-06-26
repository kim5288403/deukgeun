package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.entity.Post;
import com.example.deukgeun.trainer.request.PostRequest;
import com.example.deukgeun.trainer.response.PostResponse;
import com.example.deukgeun.trainer.service.implement.MemberServiceImpl;
import com.example.deukgeun.trainer.service.implement.PostServiceImpl;
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

    private final PostServiceImpl postService;
    private final MemberServiceImpl memberService;
    private final TokenServiceImpl tokenService;

    /**
     * 사용자 ID에 해당하는 게시물 상세 정보를 가져옵니다.
     *
     * @param id 사용자 ID
     * @return ResponseEntity 객체
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getDetailByUserId(@PathVariable("id") Long id) {
        Post post = postService.findByMemberId(id);
        PostResponse response = new PostResponse(post);

        return RestResponseUtil
                .ok("조회 성공 했습니다.", response);
    }

    /**
     * 인증 토큰을 사용하여 해당 사용자의 게시물 상세 정보를 가져옵니다.
     *
     * @param request HttpServletRequest 객체
     * @return ResponseEntity 객체
     */
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> getDetailByAuthToken(HttpServletRequest request) {
        String authToken = tokenService.resolveAuthToken(request);
        Long userId = memberService.getUserId(authToken);
        Post post = postService.findByMemberId(userId);

        PostResponse response = new PostResponse(post);

        return RestResponseUtil
                .ok("조회 성공 했습니다.", response);
    }

    /**
     * 게시글을 업로드합니다.
     *
     * @param request         HttpServletRequest 객체
     * @param postRequest     업로드할 게시글 정보를 담은 PostRequest 객체
     * @param bindingResult   데이터 유효성 검사 결과를 담은 BindingResult 객체
     * @return ResponseEntity 객체
     * @throws Exception 예외가 발생한 경우
     */
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> upload(HttpServletRequest request, @Valid PostRequest postRequest, BindingResult bindingResult) throws Exception {
        String authToken = tokenService.resolveAuthToken(request);
        postService.upload(postRequest, authToken);

        return RestResponseUtil.ok("게시글 저장 성공했습니다.", null);
    }

    /**
     * 이미지를 서버 업로드합니다.
     *
     * @param request  HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @throws Exception 예외가 발생한 경우
     */
    @RequestMapping(method = RequestMethod.POST, path = "/image")
    public void uploadServerImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<Object, Object> responseData = postService.saveImage(request, response);
        String jsonResponseData = new Gson().toJson(responseData);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponseData);
    }

    /**
     * 이미지를 서버 제거합니다.
     *
     * @param src 이미지 소스(src)
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/")
    public void deleteServerImage(@RequestParam("src") String src) {
        String path = postService.getFilePathFromUrl(src);
        File file = new File(path);
        postService.deleteFileToDirectory(file);
    }

    /**
     * 이미지를 서버에서 가져옵니다.
     *
     * @param request  HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @throws Exception 예외 발생 시
     */
    @RequestMapping(method = RequestMethod.GET, path = "/image/*")
    public void getServerImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = postService.getServerImage(request.getRequestURI());
        response.setHeader("Content-Type", request.getServletContext().getMimeType(file.getName()));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }

    /**
     * 게시글을 삭제합니다.
     *
     * @param id 삭제할 게시글의 ID
     * @return ResponseEntity 객체
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        postService.deletePost(id);

        return RestResponseUtil.ok("게시글 삭제 성공했습니다.", null);
    }
}
