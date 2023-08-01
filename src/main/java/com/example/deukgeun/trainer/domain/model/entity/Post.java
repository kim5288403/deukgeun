package com.example.deukgeun.trainer.domain.model.entity;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Post {
    private Long id;

    private String html;

    private LocalDateTime deleteDate;

    public Post(Long id, String html) {
        this.id = id;
        this.html = html;
    }

    public static Post create(String html) {
        Long id = LongIdGeneratorUtil.gen();
        return new Post(id, html);
    }

    public void updateHtml(String html) {
        this.html = html;
    }
}
