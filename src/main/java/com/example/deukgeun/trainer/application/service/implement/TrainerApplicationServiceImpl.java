package com.example.deukgeun.trainer.application.service.implement;

import com.example.deukgeun.global.util.MultipartFileUtil;
import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import com.example.deukgeun.trainer.application.dto.response.ProfileResponse;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import com.example.deukgeun.trainer.infrastructure.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerApplicationServiceImpl implements TrainerApplicationService {

    private final TrainerDomainService trainerDomainService;
    private final S3Service s3Service;

    /**
     * 주어진 이메일을 기반으로 트레이너를 검색하고 해당 트레이너의 프로필 이미지와 정보를 삭제합니다.
     *
     * @param email 삭제할 트레이너의 이메일
     * @throws IOException Amazon S3에서 이미지 삭제 시 예외 발생 가능
     */
    @Override
    public void delete(String email) throws IOException {
        Trainer trainer = findByEmail(email);
        Profile profile = trainer.getProfile();

        s3Service.delete(profile.getPath());
        trainerDomainService.deleteById(trainer.getId());
    }

    /**
     * 주어진 이메일과 라이선스 ID를 기반으로 트레이너의 라이선스를 삭제합니다.
     *
     * @param email      라이선스를 삭제할 트레이너의 이메일
     * @param licenseId  삭제할 라이선스의 고유 ID
     */
    @Override
    public void deleteLicenseByLicenseId(String email, Long licenseId) {
        trainerDomainService.deleteLicenseByLicenseId(email, licenseId);
    }

    /**
     * 주어진 이메일을 기반으로 트레이너의 게시글을 삭제하고 S3에서 관련된 이미지를 삭제합니다.
     *
     * @param email 삭제할 게시글을 소유한 트레이너의 이메일
     */
    @Override
    public void deletePostByEmail(String email) {
        trainerDomainService.deletePost(email);
    }

    /**
     * 주어진 S3 경로 또는 식별자를 사용하여 S3에서 이미지를 삭제합니다.
     *
     * @param src 삭제할 이미지의 S3 경로 또는 식별자
     */
    @Override
    public void deleteImageToS3(String src) {
        s3Service.delete(src);
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

    /**
     * 주어진 이메일에 해당하는 트레이너의 프로필 정보를 조회합니다.
     *
     * @param email 조회할 트레이너의 이메일 주소
     * @return 조회된 트레이너의 프로필 정보
     */
    @Override
    public ProfileResponse getProfileByEmail(String email) {
        Trainer trainer = findByEmail(email);
        Profile profile = trainer.getProfile();
        return new ProfileResponse(profile.getPath());
    }

    /**
     * 주어진 트레이너 ID에 해당하는 트레이너의 자격증 목록을 조회합니다.
     *
     * @param id 조회할 트레이너의 ID
     * @return 조회된 트레이너의 자격증 목록
     */
    @Override
    public List<LicenseResponse.List> getLicensesById(Long id) {
        Trainer trainer = findById(id);
        return trainer
                .getLicenses()
                .stream()
                .map(LicenseResponse.List::new)
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
        Trainer trainer = findByEmail(email);
        return trainer
                .getLicenses()
                .stream()
                .map(LicenseResponse.List::new)
                .collect(Collectors.toList());
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
        return trainerDomainService.save(request, fileName);
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
        return trainerDomainService.saveLicense(email, licenseResult);
    }

    /**
     * 클라이언트로부터 이미지 파일을 수신하여 Amazon S3에 업로드하고 업로드된 이미지의 링크를 반환합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @return 이미지 업로드된 링크를 포함하는 맵 객체
     * @throws Exception 이미지 업로드 및 유효성 검사 중 발생할 수 있는 예외
     */
    @Override
    public Map<Object, Object> saveImageToS3(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // HTTP 요청에서 파일 파트를 가져옵니다.
        Part filePart = request.getPart("file");
        // 파일의 컨텐츠 타입을 확인합니다.
        String contentType = filePart.getContentType();

        // 요청의 컨텐츠 타입이 "multipart/form-data"인지 확인합니다.
        MultipartFileUtil.validContentType(request.getContentType());

        // 파일의 MIME 타입이 허용되는 이미지 타입인지 확인합니다.
        MultipartFileUtil.validMimeType(contentType);

        // 이미지 파일을 Amazon S3에 업로드하고 업로드된 이미지의 링크를 가져옵니다.
        String filePath = s3Service.uploadByPart(filePart);

        // 업로드된 이미지의 링크를 responseData 맵에 저장합니다.
        Map<Object, Object> responseData = new HashMap<>();
        responseData.put("link", filePath);

        return responseData;
    }

    /**
     * 트레이너의 개인 정보를 업데이트합니다.
     *
     * @param request 업데이트할 개인 정보가 포함된 요청 객체
     */
    @Override
    public void updateInfo(UpdateInfoRequest request) {
        trainerDomainService.updateInfoByEmail(request);
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
        deleteImageToS3(existingPath);
    }

    /**
     * 트레이너의 비밀번호를 업데이트합니다.
     *
     * @param request 업데이트할 비밀번호 및 관련 정보가 담긴 요청 객체
     */
    @Override
    public void updatePassword(UpdatePasswordRequest request) {
        // 트레이너의 비밀번호를 업데이트합니다.
        trainerDomainService.updatePasswordByEmail(request);
    }

    /**
     * 트레이너의 게시글을 업로드합니다.
     *
     * @param email        트레이너의 이메일 주소
     * @param postRequest  게시글 업로드 요청 객체
     */
    @Override
    public void uploadPost(String email, PostRequest postRequest) {
        String content = postRequest.getContent();
        // 게시글 내용을 HTML 이스케이프하여 안전하게 저장합니다.
        String html = HtmlUtils.htmlEscape(content);
        // 트레이너의 게시글을 업로드합니다.
        trainerDomainService.uploadPostByEmail(email, html);
    }
}
