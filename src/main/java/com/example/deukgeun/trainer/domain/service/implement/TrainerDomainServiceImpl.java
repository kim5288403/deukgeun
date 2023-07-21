package com.example.deukgeun.trainer.domain.service.implement;

import com.example.deukgeun.global.util.PasswordEncoderUtil;
import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.domain.model.entity.Trainer;
import com.example.deukgeun.trainer.domain.repository.TrainerRepository;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class TrainerDomainServiceImpl implements TrainerDomainService {

    private final TrainerRepository trainerRepository;

    public void deleteById(Long id) {
        trainerRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return trainerRepository.existsByEmail(email);
    }

    public Trainer findByEmail(String email) throws EntityNotFoundException {
        return trainerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

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

    public void updateInfo(UpdateInfoRequest request) {
        Trainer trainer = trainerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        trainer.updateInfo(request);

        trainerRepository.save(trainer);
    }

    public void updatePassword(UpdatePasswordRequest request) {
        String newPassword = PasswordEncoderUtil.encode(request.getNewPassword());
        Trainer foundTrainer = trainerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        foundTrainer.updatePassword(newPassword);
        trainerRepository.save(foundTrainer);
    }

}
