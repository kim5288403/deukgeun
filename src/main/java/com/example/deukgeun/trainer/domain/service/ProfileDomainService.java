package com.example.deukgeun.trainer.domain.service;

import com.example.deukgeun.trainer.domain.model.entity.Profile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

public interface ProfileDomainService {
    void deleteById(Long profileId);
    Profile findById(Long profileId) throws EntityNotFoundException;
    Profile findByTrainerId(Long trainerId) throws EntityNotFoundException;
    Profile save(String fileName, Long trainerId) throws IOException;
    void update(Profile profile, String path);
}
