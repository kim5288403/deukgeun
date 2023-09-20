package com.example.deukgeun.trainer.application.service;

import com.example.deukgeun.trainer.application.dto.request.PostRequest;

public interface PostApplicationService {
    void deletePost(String email);
    void uploadPost(String email, PostRequest postRequest);
}
