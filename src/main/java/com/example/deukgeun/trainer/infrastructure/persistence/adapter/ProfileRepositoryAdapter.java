package com.example.deukgeun.trainer.infrastructure.persistence.adapter;

import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.repository.ProfileRepository;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.ProfileEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.repository.ProfileRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfileRepositoryAdapter implements ProfileRepository {
    private final ProfileRepositoryImpl profileRepository;

    @Override
    public void deleteById(Long profileId) {
        profileRepository.deleteById(profileId);
    }

    @Override
    public Optional<Profile> findById(Long profileId) {
        Optional<ProfileEntity> profileEntity = profileRepository.findById(profileId);
        return profileEntity.map(this::convert);
    }

    @Override
    public Optional<Profile> findByTrainerId(Long trainerId) {
        Optional<ProfileEntity> profileEntity = profileRepository.findByTrainerId(trainerId);
        return profileEntity.map(this::convert);
    }

    @Override
    public Profile save(Profile profile) {
        ProfileEntity profileEntity = profileRepository.save(convert(profile));
        return convert(profileEntity);
    }

    private Profile convert(ProfileEntity profileEntity) {
        return new Profile
                (
                        profileEntity.getId(),
                        profileEntity.getTrainerId(),
                        profileEntity.getPath()
                );
    }

    private ProfileEntity convert(Profile profile) {
        return ProfileEntity
                .builder()
                .id(profile.getId())
                .path(profile.getPath())
                .trainerId(profile.getTrainerId())
                .build();
    }
}
