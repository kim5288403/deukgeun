package com.example.deukgeun.trainer.application.service.implement;

import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.example.deukgeun.trainer.domain.model.entity.Trainer;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class TrainerApplicationServiceImpl implements TrainerApplicationService {

  private final TrainerDomainService trainerDomainService;

  public void deleteById(Long id) {
    trainerDomainService.deleteById(id);
  }

  public boolean existsByEmail(String email) {
    return trainerDomainService.existsByEmail(email);
  }

  public Trainer findByEmail(String email) throws EntityNotFoundException {
    return trainerDomainService.findByEmail(email);
  }

  public boolean isEmptyGroupName(String groupName, String groupStatus) {
    return !groupStatus.equals("Y") || !groupName.isEmpty();
  }

  public Trainer save(JoinRequest request) {
    return trainerDomainService.save(request);
  }

  public void updateInfo(UpdateInfoRequest request) {
    trainerDomainService.updateInfo(request);
  }

  public void updatePassword(UpdatePasswordRequest request) {
    trainerDomainService.updatePassword(request);
  }


}
