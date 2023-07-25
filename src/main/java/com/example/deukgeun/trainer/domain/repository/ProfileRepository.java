package com.example.deukgeun.trainer.domain.repository;

import com.example.deukgeun.trainer.domain.model.entity.Profile;

import java.util.Optional;

public interface ProfileRepository {
    void deleteById(Long profileId);
    Optional<Profile> findById(Long profileId);
    Optional<Profile> findByTrainerId(Long trainerId);
    Profile save(Profile profile);
}
