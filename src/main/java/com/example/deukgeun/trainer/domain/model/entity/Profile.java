package com.example.deukgeun.trainer.domain.model.entity;

import com.example.deukgeun.global.util.LongIdGeneratorUtil;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import lombok.Getter;

@Getter
public class Profile {

    private Long id;

    private Long trainerId;

    private String path;

    private Trainer trainer;

    public Profile (Long id, Long trainerId, String path) {
        this.id = id;
        this.trainerId = trainerId;
        this.path = path;
    }

    public Profile (Long id, Long trainerId, String path, Trainer trainer) {
        this.id = id;
        this.trainerId = trainerId;
        this.path = path;
        this.trainer = trainer;
    }
    public static Profile create(Long trainerId, String path) {
        Long id = LongIdGeneratorUtil.gen();
        return new Profile(id, trainerId, path);
    }

    public void updatePath(String path) {
        this.path = path;
    }
}
