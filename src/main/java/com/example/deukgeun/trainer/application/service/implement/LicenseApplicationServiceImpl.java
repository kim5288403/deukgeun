package com.example.deukgeun.trainer.application.service.implement;

import com.example.deukgeun.trainer.application.dto.request.RemoveLicenseRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import com.example.deukgeun.trainer.application.service.LicenseApplicationService;
import com.example.deukgeun.trainer.domain.dto.SaveLicenseDTO;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import com.example.deukgeun.trainer.infrastructure.persistence.mapper.LicenseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LicenseApplicationServiceImpl implements LicenseApplicationService {
    private final TrainerDomainService trainerDomainService;
    private final LicenseMapper licenseMapper;

    /**
     * 주어진 이메일과 라이선스 ID를 기반으로 트레이너의 라이선스를 삭제합니다.
     *
     * @param email      라이선스를 삭제할 트레이너의 이메일
     * @param removeLicenseRequest  삭제할 라이선스 Request
     */
    @Override
    public void deleteLicenseByEmailAndLicenseId(String email, RemoveLicenseRequest removeLicenseRequest) {
        removeLicenseRequest
                .getIds()
                .forEach(licenceId -> trainerDomainService.deleteLicenseByEmailAndLicenseId(email, licenceId));
    }

    /**
     * 주어진 트레이너 ID에 해당하는 트레이너의 자격증 목록을 조회합니다.
     *
     * @param id 조회할 트레이너의 ID
     * @return 조회된 트레이너의 자격증 목록
     */
    @Override
    public List<LicenseResponse.List> getLicensesById(Long id) {
        Trainer trainer = trainerDomainService.findById(id);
        return trainer
                .getLicenses()
                .stream()
                .map(licenseMapper::toLicenseResponseList)
                .collect(Collectors.toList());
    }


    /**
     * 주어진 이메일 주소에 해당하는 트레이너의 자격증 목록을 조회합니다.
     *
     * @param email 조회할 트레이너의 이메일 주소
     * @return 조회된 트레이너의 자격증 목록
     */
    @Override
    public List<LicenseResponse.List> getLicensesByEmail(String email) {
        Trainer trainer = trainerDomainService.findByEmail(email);

        return trainer
                .getLicenses()
                .stream()
                .map(licenseMapper::toLicenseResponseList)
                .collect(Collectors.toList());
    }

    /**
     * 주어진 이메일 주소와 자격증 결과 정보를 기반으로 트레이너의 자격증 정보를 등록 또는 업데이트합니다.
     *
     * @param email         트레이너의 이메일 주소
     * @param licenseResult 자격증 결과 정보
     * @return 등록 또는 업데이트된 트레이너 객체
     */
    @Override
    public Trainer saveLicense(String email, LicenseResponse.Result licenseResult) {
        SaveLicenseDTO saveLicenseDTO = licenseMapper.toSaveLicenseDto(email, licenseResult);

        return trainerDomainService.saveLicense(saveLicenseDTO);
    }

}
