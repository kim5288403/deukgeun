package com.example.deukgeun.trainer.domain.service.implement;

import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.repository.ProfileRepository;
import com.example.deukgeun.trainer.domain.service.ProfileDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfileDomainServiceImpl implements ProfileDomainService {

    private final ProfileRepository profileRepository;
    @Override
    public void deleteById(Long profileId) {
        profileRepository.deleteById(profileId);
    }
    @Override
    public Profile findById(Long profileId) throws EntityNotFoundException {
        return profileRepository.findById(profileId).orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }
    @Override
    public Profile findByTrainerId(Long trainerId) throws EntityNotFoundException {
        return profileRepository.findByTrainerId(trainerId).orElseThrow(() -> new EntityNotFoundException("프로필을 찾을 수 없습니다."));
    }

    @Override
    public Profile save(String fileName, Long trainerId) throws IOException {
        Profile profile = Profile.create(trainerId, fileName);
        return profileRepository.save(profile);
    }

    @Override
    public void update(Profile profile, String path) {
        profile.updatePath(path);
        profileRepository.save(profile);
    }
}
