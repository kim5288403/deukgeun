package com.example.deukgeun.trainer.infrastructure.persistence.adapter;

import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.repository.TrainerRepository;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.LicenseEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.TrainerEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.repository.TrainerRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TrainerRepositoryAdapter implements TrainerRepository {

    private final TrainerRepositoryImpl trainerRepository;

    @Override
    public void deleteById(Long id) {
        trainerRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return trainerRepository.existsByEmail(email);
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        Optional<TrainerEntity> trainerEntity = trainerRepository.findById(id);
        return trainerEntity.map(this::convert);
    }

    @Override
    public Optional<Trainer> findByEmail(String email) {
        Optional<TrainerEntity> trainerEntity = trainerRepository.findByEmail(email);
        return trainerEntity.map(this::convert);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return trainerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을수 없습니다."));
    }

    @Override
    public Trainer save(Trainer trainer) {
        TrainerEntity saveTrainerEntity = trainerRepository.save(convert(trainer));

        return convert(saveTrainerEntity);
    }

    private TrainerEntity convert(Trainer trainer) {
        return TrainerEntity
                .builder()
                .id(trainer.getId())
                .name(trainer.getName())
                .email(trainer.getEmail())
                .password(trainer.getPassword())
                .groupStatus(trainer.getGroupStatus())
                .groupName(trainer.getGroupName())
                .postcode(trainer.getPostcode())
                .jibunAddress(trainer.getJibunAddress())
                .roadAddress(trainer.getRoadAddress())
                .detailAddress(trainer.getDetailAddress())
                .extraAddress(trainer.getExtraAddress())
                .gender(trainer.getGender())
                .price(trainer.getPrice())
                .introduction(trainer.getIntroduction())
                .build();
    }

    private Trainer convert(TrainerEntity trainerEntity) {
        return new Trainer(
                trainerEntity.getId(),
                trainerEntity.getName(),
                trainerEntity.getEmail(),
                trainerEntity.getPassword(),
                trainerEntity.getGroupStatus(),
                trainerEntity.getGroupName(),
                trainerEntity.getPostcode(),
                trainerEntity.getJibunAddress(),
                trainerEntity.getRoadAddress(),
                trainerEntity.getDetailAddress(),
                trainerEntity.getExtraAddress(),
                trainerEntity.getGender(),
                trainerEntity.getPrice(),
                trainerEntity.getIntroduction(),
                trainerEntity.getLicenseEntities().stream().map(this::convert).collect(Collectors.toList())
        );
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
}
