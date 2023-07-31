package com.example.deukgeun.trainer.application.service.implement;

import com.example.deukgeun.global.util.MultipartFileUtil;
import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.response.LicenseResponse;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerApplicationServiceImpl implements TrainerApplicationService {

    private final TrainerDomainService trainerDomainService;

    @Value("${trainer.profile.filePath}")
    private String PROFILE_FILE_PATH;

    @Value("${trainer.post.filePath}")
    private String POST_FILE_PATH;

    @Value("${trainer.post.url}")
    private String POST_URL;

    @Override
    public void delete(String email) throws IOException {
        Trainer trainer = findByEmail(email);
        Profile profile = trainer.getProfile();

        MultipartFileUtil.deleteFileToDirectory(profile.getPath(), PROFILE_FILE_PATH);

        trainerDomainService.deleteById(trainer.getId());
    }

    @Override
    public void deleteLicenseByLicenseId(String email, Long licenseId) {
        trainerDomainService.deleteLicenseByLicenseId(email, licenseId);
    }

    @Override
    public void deletePost(String email) {
        trainerDomainService.deletePost(email);
    }

    @Override
    public void deleteImageToServer(String src) throws IOException {
        String path = MultipartFileUtil.getFilePathFromUrl(src, POST_FILE_PATH);
        MultipartFileUtil.deleteFileToDirectory(path, POST_FILE_PATH);
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
    public File getServerImage(String url) {
        return MultipartFileUtil.getServerImage(url, POST_FILE_PATH);
    }

    @Override
    public List<LicenseResponse.List> getLicensesById(Long id) {
        Trainer trainer = findById(id);
        return trainer
                .getLicenses()
                .stream()
                .map(LicenseResponse.List::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<LicenseResponse.List> getLicensesByEmail(String email) {
        Trainer trainer = findByEmail(email);
        return trainer
                .getLicenses()
                .stream()
                .map(LicenseResponse.List::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isEmptyGroupName(String groupName, String groupStatus) {
        return !groupStatus.equals("Y") || !groupName.isEmpty();
    }

    @Override
    public Trainer save(JoinRequest request) throws IOException {
        String fileName = MultipartFileUtil.getUUIDPath(request.getProfile().getOriginalFilename());
        return trainerDomainService.save(request, fileName);
    }

    @Override
    public Trainer saveLicense(String email, LicenseResponse.Result licenseResult) {
        return trainerDomainService.saveLicense(email, licenseResult);
    }

    @Override
    public Map<Object, Object> saveImageToServer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 이미지를 저장할 디렉토리 생성
        File uploads = new File(POST_FILE_PATH);
        Part filePart = request.getPart("file");
        String contentType = filePart.getContentType();

        // 요청의 컨텐츠 타입 검증
        MultipartFileUtil.validContentType(request.getContentType());

        // 파일 확장자 추출
        String extension = MultipartFileUtil.getExtensionFromContentType(contentType);
        String name = UUID.randomUUID() + extension;
        String linkName = POST_URL + name;

        // MIME 타입 검증
        MultipartFileUtil.validMimeType(contentType, name, POST_FILE_PATH);

        File file = new File(uploads, name);
        MultipartFileUtil.saveServerImage(filePart, response.getWriter(), file);

        // 이미지 링크를 담은 맵 생성하여 반환
        Map<Object, Object> responseData = new HashMap<>();
        responseData.put("link", linkName);

        return responseData;
    }

    @Override
    public void updateInfo(UpdateInfoRequest request) {
        trainerDomainService.updateInfo(request);
    }

    @Override
    public void updateProfile(String email, MultipartFile profile) throws IOException {
        String fileName = MultipartFileUtil.getUUIDPath(profile.getOriginalFilename());
        MultipartFileUtil.saveFileToDirectory(profile, fileName, PROFILE_FILE_PATH);

        Trainer trainer = findByEmail(email);
        MultipartFileUtil.deleteFileToDirectory(trainer.getProfile().getPath(), PROFILE_FILE_PATH);

        trainerDomainService.updateProfile(trainer, fileName);
    }

    @Override
    public void updatePassword(UpdatePasswordRequest request) {
        trainerDomainService.updatePassword(request);
    }

    @Override
    public void uploadPost(String email, PostRequest postRequest) {
        String content = postRequest.getContent();
        String html = HtmlUtils.htmlEscape(content);
        trainerDomainService.uploadPost(email, html);
    }
}
