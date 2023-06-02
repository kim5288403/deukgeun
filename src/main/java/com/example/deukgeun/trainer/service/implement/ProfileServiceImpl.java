package com.example.deukgeun.trainer.service.implement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
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

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UserServiceImpl userService;

    @Value("${trainer.profile.filePath}")
    private String FILE_PATH;

    //file name
    private String fileName;

    /**
     * 주어진 프로필 ID에 해당하는 프로필을 조회합니다.
     *
     * @param profileId 조회할 프로필의 ID
     * @return 조회된 프로필
     * @throws EntityNotFoundException 프로필을 찾을 수 없을 경우 예외가 발생합니다.
     */
    @Cacheable(value = "profile", key = "#profileId", cacheManager = "projectCacheManager", unless = "#result == null")
    public Profile getProfile(Long profileId) throws EntityNotFoundException {
        return profileRepository.findById(profileId).orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }

    /**
     * 주어진 인증 토큰을 사용하여 사용자의 프로필 ID를 조회합니다.
     *
     * @param authToken 조회할 사용자의 인증 토큰
     * @return 사용자의 프로필 ID
     * @throws Exception 프로필을 조회하는 도중 예외가 발생한 경우
     */
    public Long getProfileId(String authToken) {
        User user = userService.getUserByAuthToken(authToken);
        Profile profile = getByUserId(user.getId());

        return profile.getId();
    }

    /**
     * 주어진 사용자 ID에 해당하는 프로필을 조회합니다.
     *
     * @param userId 조회할 프로필의 사용자 ID
     * @return 조회된 프로필
     * @throws EntityNotFoundException 프로필을 찾을 수 없는 경우 발생하는 예외
     */
    public Profile getByUserId(Long userId) throws EntityNotFoundException {
        return profileRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("프로필을 찾을 수 없습니다."));
    }

    /**
     * 주어진 MultipartFile 의 컨텐츠 타입이 지원되는 이미지 타입인지 확인합니다.
     *
     * @param file 확인할 MultipartFile 객체
     * @return 지원되는 이미지 타입인 경우 true, 그렇지 않은 경우 false 를 반환합니다.
     */
    public boolean isSupportedContentType(MultipartFile file) {
        String fileType = Objects.requireNonNull(file.getContentType());
        return fileType.equals("image/png") || fileType.equals("image/jpg") || fileType.equals("image/jpeg");
    }

    /**
     * 주어진 MultipartFile 을 지정된 디렉토리에 저장합니다.
     *
     * @param profile  저장할 MultipartFile 객체
     * @param fileName 저장할 파일명
     * @throws IOException 파일 저장 중에 발생한 IO 예외가 처리될 수 있습니다.
     */
    public void saveFileToDirectory(MultipartFile profile, String fileName) throws IOException {
        Path path = Paths.get(FILE_PATH).toAbsolutePath().normalize();
        Path targetPath = path.resolve(fileName).normalize();

        profile.transferTo(targetPath);
    }

    /**
     * 지정된 파일명을 가진 파일을 디렉토리에서 삭제합니다.
     *
     * @param fileName 삭제할 파일명
     */
    public void deleteFileToDirectory(String fileName) {
        File file = new File(FILE_PATH + "\\" + fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 프로필 이미지를 저장하고 관련 데이터를 데이터베이스에 저장합니다.
     *
     * @param profile 업로드된 프로필 이미지 파일
     * @param userId  사용자 ID
     * @throws IOException 입출력 예외가 발생할 경우
     */
    public void save(MultipartFile profile, Long userId) throws IOException {
        fileName = getUUIDPath(profile.getOriginalFilename());

        Profile saveProfileData = ProfileRequest.create(fileName, userId);
        profileRepository.save(saveProfileData);
        saveFileToDirectory(profile, fileName);
    }


    /**
     * 프로필 이미지를 업데이트하고 관련 데이터를 갱신합니다.
     *
     * @param profile   업로드된 프로필 이미지 파일
     * @param authToken 인증 토큰
     * @throws Exception 예외가 발생할 경우
     */
    @Transactional
    public void updateProfile(MultipartFile profile, String authToken) throws Exception {
        Long profileId = getProfileId(authToken);
        fileName = getUUIDPath(profile.getOriginalFilename());

        saveFileToDirectory(profile, fileName);

        Profile userProfile = getProfile(profileId);
        deleteFileToDirectory(userProfile.getPath());

        update(profileId, fileName);
    }

    /**
     * 프로필 ID에 해당하는 프로필을 업데이트합니다.
     *
     * @param profileId 프로필 ID
     */
    @CachePut(value = "profile", key = "#profileId", cacheManager = "projectCacheManager", unless="#result == null")
    public void update(Long profileId, String path) {
        profileRepository.updateProfile(profileId, path);
    }

    /**
     * 프로필 ID에 해당하는 프로필을 삭제합니다.
     *
     * @param profileId 프로필 ID
     */
    @CacheEvict(value = "profile", key = "#profileId", cacheManager = "projectCacheManager")
    public void withdrawal(Long profileId) {
        profileRepository.deleteById(profileId);
    }

    /**
     * 파일 이름에 UUID 를 추가한 경로를 반환합니다.
     *
     * @param fileName 파일 이름
     * @return UUID 가 추가된 파일 경로
     * @throws IOException 파일 이름이 비어있는 경우 예외 발생
     */
    public String getUUIDPath(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IOException("Empty fileName");
        }

        String uuid = UUID.randomUUID().toString();

        return uuid + "_" + fileName;
    }
}
