package com.example.deukgeun.trainer.application.service.implement;

import com.example.deukgeun.global.util.MultipartFileUtil;
import com.example.deukgeun.trainer.application.dto.response.ProfileResponse;
import com.example.deukgeun.trainer.application.service.ProfileApplicationService;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import com.example.deukgeun.trainer.infrastructure.persistence.mapper.ProfileMapper;
import com.example.deukgeun.trainer.infrastructure.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProfileApplicationServiceImpl implements ProfileApplicationService {
    private final TrainerDomainService trainerDomainService;
    private final S3Service s3Service;
    private final ProfileMapper profileMapper;

    /**
     * 주어진 ID 해당하는 트레이너의 프로필 정보를 조회합니다.
     * @param id
     * @return 조회된 트레이너의 프로필 정보
     */
    @Override
    public ProfileResponse getProfileId(Long id) {
        Trainer trainer = trainerDomainService.findById(id);
        return profileMapper.toProfileResponse(trainer.getProfile());
    }

    /**
     * 주어진 이메일에 해당하는 트레이너의 프로필 정보를 조회합니다.
     *
     * @param email 조회할 트레이너의 이메일 주소
     * @return 조회된 트레이너의 프로필 정보
     */
    @Override
    public ProfileResponse getProfileByEmail(String email) {
        Trainer trainer = trainerDomainService.findByEmail(email);
        return profileMapper.toProfileResponse(trainer.getProfile());
    }

    /**
     * 트레이너의 프로필 사진을 업데이트합니다.
     *
     * @param email 트레이너의 이메일 주소
     * @param file  업데이트할 프로필 사진 파일
     * @throws Exception 업데이트 과정에서 예외가 발생할 경우
     */
    @Override
    public void updateProfile(String email, MultipartFile file) throws Exception {
        // 업데이트할 프로필 사진 파일의 MIME 유형을 검증합니다.
        MultipartFileUtil.validMimeType(Objects.requireNonNull(file.getContentType()));

        // S3에 새로운 사진을 업로드합니다.
        String fileName = s3Service.uploadByMultiPartFile(file);

        // 트레이너의 프로필 사진을 업데이트하고 기존 사진 경로를 리턴 받습니다.
        String existingPath = trainerDomainService.updateProfileByEmail(email, fileName);

        // 기존 프로필 사진을 삭제합니다.
        s3Service.delete(existingPath);
    }
}
