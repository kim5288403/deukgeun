package com.example.deukgeun.trainer.service.implement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Value("${trainer.profile.filePath}")
    private String FILE_PATH;

    @Cacheable(value = "profile", key = "#profileId", cacheManager = "projectCacheManager")
    public Profile getProfile(Long profileId) throws Exception {
        return profileRepository.findById(profileId).orElseThrow(() -> new Exception("게시글을 찾을 수 없습니다."));
    }

    public Long getProfileId(String authToken) throws Exception {
        User user = userService.getUserByAuthToken(authToken);
        Profile profile = getByUserId(user.getId());

        return profile.getId();
    }

    public Profile getByUserId(Long userId) throws Exception {
        return profileRepository.findByUserId(userId).orElseThrow(() -> new Exception("프로필을 찾을 수 없습니다."));
    }

    public boolean isSupportedContentType(String fileType) {
        return fileType.equals("image/png") || fileType.equals("image/jpg") || fileType.equals("image/jpeg");
    }

    public void saveServer(MultipartFile profile, String filename) {
        try {
            Path path = Paths.get(FILE_PATH).toAbsolutePath().normalize();
            Path targetPath = path.resolve(filename).normalize();

            profile.transferTo(targetPath);
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
        String path = getUUIDPath(profile.getOriginalFilename());

        Profile saveProfileData = ProfileRequest.create(path, userId);
        profileRepository.save(saveProfileData);
        saveServer(profile, path);
    }
    @Transactional
    public void updateProfile(MultipartFile profile, String authToken) throws Exception {
        Long profileId = getProfileId(authToken);
        String path = getUUIDPath(profile.getOriginalFilename());

        //DB 수정
        update(profileId, path);

        //서버 저장
        saveServer(profile, path);

        //server 저장된 파일 삭제
        Profile userProfile = getProfile(profileId);
        deleteServer(userProfile.getPath());
    }

    @CachePut(value = "profile", key = "#profileId", cacheManager = "projectCacheManager")
    public void update(Long profileId, String path) {
        profileRepository.updateProfile(profileId, path);
    }

    @CacheEvict(value = "profile", key = "#profileId", cacheManager = "projectCacheManager")
    public void withdrawal(Long profileId) {
        profileRepository.deleteById(profileId);
    }

    public String getUUIDPath(String fileName) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + "_" + fileName;
    }
}
