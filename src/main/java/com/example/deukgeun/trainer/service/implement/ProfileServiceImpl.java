package com.example.deukgeun.trainer.service.implement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.ProfileRepository;
import com.example.deukgeun.trainer.request.ProfileRequest;
import com.example.deukgeun.trainer.service.ProfileService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UserServiceImpl userService;

    //트레이너 profile 저장 경로
    @Value("${trainer.profile.filePath}")
    private String FILE_PATH;

    public Profile getProfile(Long profileId) throws Exception {
        return profileRepository.findById(profileId).orElseThrow(() -> new Exception("게시글을 찾을 수 없습니다."));
    }

    public Long getProfileId(String authToken) throws Exception {
        User user = userService.getUserByAuthToken(authToken);
        Profile profile = profileRepository.findByUserId(user.getId()).orElseThrow(() -> new Exception("프로필을 찾을 수 없습니다."));

        return profile.getId();
    }

    public Profile getProfileByUserId(Long userId) throws Exception {
        return profileRepository.findByUserId(userId).orElseThrow(() -> new Exception("프로필을 찾을 수 없습니다."));
    }

    public boolean isSupportedContentType(String fileType) {
        return fileType.equals("image/png") || fileType.equals("image/jpg") || fileType.equals("image/jpeg");
    }

    /**
     * server 에 file 저장됨
     *
     * @param profile
     * @param filename
     * @return
     */
    public String saveServer(MultipartFile profile, String filename) {
        try {
            Path path = Paths.get(FILE_PATH).toAbsolutePath().normalize();
            Path targetPath = path.resolve(filename).normalize();

            profile.transferTo(targetPath);
            return filename;
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 업로드에 실패했습니다.");
        }
    }

    public void deleteServer(String path) {
        File file = new File(FILE_PATH + "\\" + path);
        if (file.exists()) {
            file.delete();
        }
    }

    public void save(MultipartFile profile, Long userId) {
        UUID uuid = UUID.randomUUID();
        String path = uuid.toString() + "_" + profile.getOriginalFilename();

        Profile saveProfileData = ProfileRequest.create(path, userId);
        profileRepository.save(saveProfileData);
        saveServer(profile, path);
    }

    public void updateProfile(Long profileId, String path) {
        profileRepository.updateProfile(profileId, path);
    }

    public void withdrawal(Long profileId) {
        profileRepository.deleteById(profileId);
    }
}
