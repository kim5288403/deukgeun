package com.example.deukgeun.trainer.application.service.implement;

import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResultResponse;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerApplicationServiceImpl implements TrainerApplicationService {

  private final TrainerDomainService trainerDomainService;

  @Override
  public void deleteById(Long id) {
    trainerDomainService.deleteById(id);
  }

  @Override
  public void deleteLicenseByLicenseId(String email, Long licenseId) {
    trainerDomainService.deleteLicenseByLicenseId(email, licenseId);
  }

  @Override
  public boolean existsByEmail(String email) {
    return trainerDomainService.existsByEmail(email);
  }

  @Override
  public Trainer findById(Long id) {
    return trainerDomainService.findById(id);
  }

  @Override
  public Trainer findByEmail(String email) {
    return trainerDomainService.findByEmail(email);
  }

  @Override
  public boolean isEmptyGroupName(String groupName, String groupStatus) {
    return !groupStatus.equals("Y") || !groupName.isEmpty();
  }

  @Override
  public Trainer save(JoinRequest request) {
    return trainerDomainService.save(request);
  }

  @Override
  public Trainer saveLicense(String email, LicenseResultResponse licenseResult) {
    return trainerDomainService.saveLicense(email, licenseResult);
  }

  @Override
  public void updateInfo(UpdateInfoRequest request) {
    trainerDomainService.updateInfo(request);
  }

  @Override
  public void updatePassword(UpdatePasswordRequest request) {
    trainerDomainService.updatePassword(request);
  }


}
