package com.example.deukgeun.trainer.infrastructure.persistence.adapter;

import com.example.deukgeun.trainer.application.dto.response.LicenseListResponse;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.repository.LicenseRepository;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.LicenseEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.repository.LicenseRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LicenseRepositoryAdapter implements LicenseRepository {

    private final LicenseRepositoryImpl licenseRepository;
    @Override
    public void deleteById(Long id) {
        licenseRepository.deleteById(id);
    }

    @Override
    public List<LicenseListResponse> findByTrainerId(Long trainerId) {
        return licenseRepository.findByTrainerId(trainerId)
                .stream()
                .map(this::convertToListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public License save(License license) {
        LicenseEntity licenseEntity = licenseRepository.save(convert(license));
        return convert(licenseEntity);
    }

    private LicenseEntity convert(License license) {
        return LicenseEntity
                .builder()
                .id(license.getId())
                .certificateName(license.getCertificateName())
                .licenseNumber(license.getLicenseNumber())
                .trainerId(license.getTrainerId())
                .build();
    }

    private License convert(LicenseEntity licenseEntity) {
        return new License(
                licenseEntity.getId(),
                licenseEntity.getCertificateName(),
                licenseEntity.getLicenseNumber(),
                licenseEntity.getTrainerId()
        );
    }

    public LicenseListResponse convertToListResponse(LicenseEntity licenseEntity) {
        return new LicenseListResponse
                (
                        licenseEntity.getId(),
                        licenseEntity.getCertificateName(),
                        licenseEntity.getLicenseNumber(),
                        licenseEntity.getCreatedDate()
                );
    }

}
