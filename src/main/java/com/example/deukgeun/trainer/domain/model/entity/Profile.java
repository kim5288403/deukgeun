package com.example.deukgeun.trainer.domain.model.entity;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import lombok.Getter;

@Getter
public class Profile {

    private Long id;

    private String path;

    public Profile (Long id, String path) {
        this.id = id;
        this.path = path;
    }

    public static Profile create(String path) {
        Long id = LongIdGeneratorUtil.gen();
        return new Profile(id, path);
    }

    public void updatePath(String path) {
        this.path = path;
    }
}
