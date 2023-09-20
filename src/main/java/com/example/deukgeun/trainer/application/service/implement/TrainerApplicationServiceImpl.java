package com.example.deukgeun.trainer.application.service.implement;

import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.response.TrainerResponse;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import com.example.deukgeun.trainer.infrastructure.persistence.mapper.TrainerMapper;
import com.example.deukgeun.trainer.infrastructure.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TrainerApplicationServiceImpl implements TrainerApplicationService {

    private final TrainerDomainService trainerDomainService;
    private final S3Service s3Service;
    private final TrainerMapper trainerMapper;

    /**
     * 주어진 이메일을 기반으로 트레이너를 검색하고 해당 트레이너의 프로필 이미지와 정보를 삭제합니다.
     *
     * @param email 삭제할 트레이너의 이메일
     */
    @Override
    public void delete(String email) {
        Trainer trainer = findByEmail(email);
        Profile profile = trainer.getProfile();

        s3Service.delete(profile.getPath());
        trainerDomainService.deleteById(trainer.getId());
    }

    /**
     * 주어진 이메일 주소로 등록된 트레이너가 존재하는지 여부를 확인합니다.
     *
     * @param email 확인할 이메일 주소
     * @return 트레이너가 존재하면 true, 그렇지 않으면 false
     */
    @Override
    public boolean existsByEmail(String email) {
        return trainerDomainService.existsByEmail(email);
    }

    /**
     * 주어진 ID에 해당하는 트레이너 정보를 조회합니다.
     *
     * @param id 조회할 트레이너의 ID
     * @return 조회된 트레이너 정보
     */
    @Override
    public Trainer findById(Long id) {
        return trainerDomainService.findById(id);
    }

    /**
     * 주어진 이메일에 해당하는 트레이너 정보를 조회합니다.
     *
     * @param email 조회할 트레이너의 이메일 주소
     * @return 조회된 트레이너 정보
     */
    @Override
    public Trainer findByEmail(String email) {
        return trainerDomainService.findByEmail(email);
    }

    @Override
    public TrainerResponse.Info getInfoByEmail(String email) {
        Trainer trainer = findByEmail(email);
        return trainerMapper.toTrainerInfoResponse(trainer);
    }

    @Override
    public TrainerResponse.Detail getDetailByEmail(String email) {
        Trainer trainer = findByEmail(email);
        return trainerMapper.toTrainerDetailResponse(trainer);
    }

    /**
     * 주어진 회원 가입 요청 정보를 기반으로 새로운 트레이너를 등록하고 프로필 이미지를 S3에 업로드합니다.
     *
     * @param request 회원 가입 요청 정보
     * @return 등록된 트레이너 객체
     * @throws IOException 프로필 이미지 업로드 중에 발생할 수 있는 입출력 예외
     */
    @Override
    public Trainer save(JoinRequest request) throws IOException {
        String fileName = s3Service.uploadByMultiPartFile(request.getProfile());
        return trainerDomainService.save(trainerMapper.toSaveTrainerDto(fileName, request));
    }

    /**
     * 트레이너의 개인 정보를 업데이트합니다.
     *
     * @param request 업데이트할 개인 정보가 포함된 요청 객체
     */
    @Override
    public void updateInfo(UpdateInfoRequest request) {
        trainerDomainService.updateInfoByEmail(trainerMapper.toUpdateInfoDto(request));
    }

    /**
     * 트레이너의 비밀번호를 업데이트합니다.
     *
     * @param request 업데이트할 비밀번호 및 관련 정보가 담긴 요청 객체
     */
    @Override
    public void updatePassword(UpdatePasswordRequest request) {
        trainerDomainService.updatePasswordByEmail(trainerMapper.toUpdatePasswordDto(request));
    }
}
