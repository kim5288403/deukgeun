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

    //file name
    private String fileName;

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

    public void saveServer(MultipartFile profile) throws IOException {
        Path path = Paths.get(FILE_PATH).toAbsolutePath().normalize();
        Path targetPath = path.resolve(fileName).normalize();

        profile.transferTo(targetPath);
    }

    public void deleteServer(String path) {
        File file = new File(FILE_PATH + "\\" + path);
        if (file.exists()) {
            file.delete();
        }
    }

    public void save(MultipartFile profile, Long userId) throws IOException {
        fileName = getUUIDPath(profile.getOriginalFilename());

        Profile saveProfileData = ProfileRequest.create(fileName, userId);
        profileRepository.save(saveProfileData);
        saveServer(profile);
    }

    @Transactional
    public void updateProfile(MultipartFile profile, String authToken) throws Exception {
        Long profileId = getProfileId(authToken);
        fileName = getUUIDPath(profile.getOriginalFilename());

        //DB 수정
        update(profileId);

        //서버 저장
        saveServer(profile);

        //server 저장된 파일 삭제
        Profile userProfile = getProfile(profileId);
        deleteServer(userProfile.getPath());
    }

    @CachePut(value = "profile", key = "#profileId", cacheManager = "projectCacheManager")
    public void update(Long profileId) {
        profileRepository.updateProfile(profileId, fileName);
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
