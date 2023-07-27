package com.example.deukgeun.trainer.domain.service.implement;

import com.example.deukgeun.global.util.PasswordEncoderUtil;
import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResultResponse;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.License;
import com.example.deukgeun.trainer.domain.repository.TrainerRepository;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class TrainerDomainServiceImpl implements TrainerDomainService {

    private final TrainerRepository trainerRepository;

    @Override
    public void deleteById(Long id) {
        trainerRepository.deleteById(id);
    }

    @Override
    public void deleteLicenseByLicenseId(String email, Long licenseId) {
        Trainer trainer = trainerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        License license = trainer.getLicenses()
                .stream()
                .filter(item -> item.getId().equals(licenseId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("자격증을 찾을 수 없습니다."));

        trainer.getLicenses().remove(license);

        trainerRepository.save(trainer);
    }

    @Override
    public boolean existsByEmail(String email) {
        return trainerRepository.existsByEmail(email);
    }

    @Override
    public Trainer findById(Long id) throws EntityNotFoundException {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public Trainer findByEmail(String email) throws EntityNotFoundException {
        return trainerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Override
    public Trainer save(JoinRequest request) {
        Trainer trainer = Trainer.create(
                request.getName(),
                request.getEmail(),
                PasswordEncoderUtil.encode(request.getPassword()),
                request.getGroupStatus(),
                request.getGroupName(),
                request.getPostcode(),
                request.getJibunAddress(),
                request.getRoadAddress(),
                request.getDetailAddress(),
                request.getExtraAddress(),
                request.getGender(),
                request.getPrice(),
                request.getIntroduction()
        );

        return trainerRepository.save(trainer);
    }

    @Override
    public Trainer saveLicense(String email, LicenseResultResponse licenseResult) {
        Trainer trainer = findByEmail(email);
        License license = License.create(licenseResult.getCertificatename(), licenseResult.getNo(), trainer.getId());
        trainer.getLicenses().add(license);

        return trainerRepository.save(trainer);
    }

    @Override
    public void updateInfo(UpdateInfoRequest request) {
        Trainer trainer = trainerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        trainer.updateInfo(request);

        trainerRepository.save(trainer);
    }

    @Override
    public void updatePassword(UpdatePasswordRequest request) {
        String newPassword = PasswordEncoderUtil.encode(request.getNewPassword());
        Trainer foundTrainer = trainerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        foundTrainer.updatePassword(newPassword);
        trainerRepository.save(foundTrainer);
    }

}
