package com.example.deukgeun.trainer.application.service.implement;

import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.application.service.PostApplicationService;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

@Service
@RequiredArgsConstructor
public class PostApplicationServiceImpl implements PostApplicationService {
    private final TrainerDomainService trainerDomainService;

    /**
     * 주어진 이메일을 기반으로 트레이너의 게시글을 삭제하고 S3에서 관련된 이미지를 삭제합니다.
     *
     * @param email 삭제할 게시글을 소유한 트레이너의 이메일
     */
    @Override
    public void deletePost(String email) {
        trainerDomainService.deletePostByEmail(email);
    }

    /**
     * 트레이너의 게시글을 업로드합니다.
     *
     * @param email        트레이너의 이메일 주소
     * @param postRequest  게시글 업로드 요청 객체
     */
    @Override
    public void uploadPost(String email, PostRequest postRequest) {
        String content = postRequest.getContent();
        // 게시글 내용을 HTML 이스케이프하여 안전하게 저장합니다.
        String html = HtmlUtils.htmlEscape(content);
        // 트레이너의 게시글을 업로드합니다.
        trainerDomainService.uploadPostByEmail(email, html);
    }
}
