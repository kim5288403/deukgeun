package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import com.example.deukgeun.commom.service.implement.ValidateServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.entity.Post;
import com.example.deukgeun.trainer.request.PostRequest;
import com.example.deukgeun.trainer.response.PostResponse;
import com.example.deukgeun.trainer.service.implement.PostServiceImpl;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.nio.file.Files;
import java.util.Map;

@RequestMapping("/api/trainer/post")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostServiceImpl postService;
    private final UserServiceImpl userService;
    private final ValidateServiceImpl validateService;
    private final JwtServiceImpl jwtService;

    /**
     * 게시글 상세보기
     * user_id 해당되는 data
     *
     * @param id 트레이너 user_id
     * @return RestResponseUtil post data
     * @throws Exception - 게시글을 찾을 수 없습니다.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getDetailByUserId(@PathVariable("id") Long id) throws Exception {
        Post post = postService.findByUserId(id);
        PostResponse response = new PostResponse(post);

        return RestResponseUtil
                .okResponse("조회 성공 했습니다.", response);
    }

    /**
     * 게시글 상세보기
     * authToken 에서 추출한 user data 로 userId를 구해 데이터 비교
     *
     * @param request authToken 추출을 위한 파라미터
     * @return  RestResponseUtil post data
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ResponseEntity<?> getDetailByAuthToken(HttpServletRequest request) throws Exception {
        String authToken = jwtService.resolveAuthToken(request);
        Long userId = userService.getUserId(authToken);
        Post post = postService.findByUserId(userId);
        PostResponse response = new PostResponse(post);

        return RestResponseUtil
                .okResponse("조회 성공 했습니다.", response);
    }

    /**
     * 게시글 저장 및 업데이트
     * 게시글이 존재하면 update, 존재하지 않으면 save
     *
     * @param request authToken 추출을 위한 파라미터
     * @param postRequest 게시글 form data
     * @param bindingResult  form data validator 결과를 담는 역할
     * @return RestResponseUtil
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, path = "/")
    public ResponseEntity<?> upload(HttpServletRequest request, @Valid PostRequest postRequest, BindingResult bindingResult) throws Exception {
        System.out.println("gd");
        validateService.errorMessageHandling(bindingResult);
        String authToken = jwtService.resolveAuthToken(request);
        postService.upload(postRequest, authToken);

        return RestResponseUtil.okResponse("게시글 저장 성공했습니다.", null);
    }

    /**
     * 서버 image 저장
     * 게시글에 사용된 image 를 서버에 저장
     *
     * @param request image file data 추출을 위한 파라미터
     * @param response 저장된 image json 형식으로 link 를 반환
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, path = "/image")
    public void uploadImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<Object, Object> responseData = postService.saveImage(request, response);
        String jsonResponseData = new Gson().toJson(responseData);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponseData);
    }

    /**
     * 서버 image 삭제
     *
     * @param src image path
     */
    @RequestMapping(method = RequestMethod.POST, path = "/remove")
    public void removeImage(@RequestParam("src") String src) {
        postService.deletePostImage(src);
    }

    /**
     * 서버 image 가져오기
     * request url 에서 이미지 path 를 추출해 서버에 존재 유무를 통한
     *
     * @param request 통신 url 추출을 위한 파라미터
     * @param response 추출한 file data 를 반환하기 위한 파라미터
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, path = "/image/*")
    public void getImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = postService.getServerImage(request.getRequestURI());
        response.setHeader("Content-Type", request.getServletContext().getMimeType(file.getName()));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
    }

    /**
     * 게시글 삭제
     *
     * @param id 게시글 id
     * @return RestResponseUtil
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public ResponseEntity<?> remove(@PathVariable("id") Long id){
        postService.deletePost(id);

        return RestResponseUtil.okResponse("게시글 삭제 성공했습니다.", null);
    }

}
