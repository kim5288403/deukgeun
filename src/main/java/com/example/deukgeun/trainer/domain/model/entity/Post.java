package com.example.deukgeun.trainer.domain.model.entity;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.Getter;

@Getter
public class Post {
    private Long id;

    private Long trainerId;

    private String html;

    public Post(Long id, String html, Long trainerId) {
        this.id = id;
        this.html = html;
        this.trainerId = trainerId;
    }

    public static Post create(String html, Long trainerId) {
        Long id = LongIdGeneratorUtil.gen();
        return new Post(id, html, trainerId);
    }

    public void updateHtml(String html) {
        this.html = html;
    }
}
