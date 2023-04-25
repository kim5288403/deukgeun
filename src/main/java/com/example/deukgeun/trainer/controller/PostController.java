package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.entity.Post;
import com.example.deukgeun.trainer.response.PostResponse;
import com.example.deukgeun.trainer.service.implement.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/trainer/post")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostServiceImpl postService;

    /**
     * 게시글 상세보기
     *
     * @param Long id 트레이너 user_id
     * @return user_id에 해당된 post 데이터
     * @throws Exception - 게시글을 찾을 수 없습니다.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getDetail(@PathVariable("id") Long id) throws Exception {
        Post post = postService.findByUserId(id);
        PostResponse response = new PostResponse(post);

        return RestResponseUtil
                .okResponse("조회 성공 했습니다.", response);
    }

}
