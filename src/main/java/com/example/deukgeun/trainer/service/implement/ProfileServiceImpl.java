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
     * profile data 가져오기
     * profileId에 해당된 profile data 가져오기
     *
     * @param profileId profileId 비교를 위한 파라미터
     * @return profileId에 해당된 profile data
     * @throws Exception 일치하는 데이터가 없을 경우 Exception 발생
     */
    @Cacheable(value = "profile", key = "#profileId", cacheManager = "projectCacheManager")
    public Profile getProfile(Long profileId) throws Exception {
        return profileRepository.findById(profileId).orElseThrow(() -> new Exception("게시글을 찾을 수 없습니다."));
    }

    /**
     * profile data 에서 profile id 가져오기
     * authToken 으로 가져온 User 데이터와 일치하는 프로필 데이터에서 아이디 반환
     *
     * @param authToken JWT 인증토큰
     * @return authToken 으로 가져온 User 데이터와 일치하는 프로필 데이터에서 아이디
     * @throws Exception 일치하는 데이터가 없을 경우 Exception 발생
     */
    public Long getProfileId(String authToken) throws Exception {
        User user = userService.getUserByAuthToken(authToken);
        Profile profile = getByUserId(user.getId());

        return profile.getId();
    }

    /**
     * profile data 가져오기
     * user id와 일치하는 profile data 가져오기
     *
     * @param userId user id를 비교하기 위한 파라미터
     * @return profile data
     * @throws Exception 일치하는 데이터가 없을 경우 Exception 발생
     */
    public Profile getByUserId(Long userId) throws Exception {
        return profileRepository.findByUserId(userId).orElseThrow(() -> new Exception("프로필을 찾을 수 없습니다."));
    }

    /**
     * file 확장자 type 유효성 검사
     *
     * @param file 확장자 type 유효성 검사를 위한 파라미터
     * @return 확장자 type 유효성 검사 결과
     */
    public boolean isSupportedContentType(MultipartFile file) {
        String fileType = Objects.requireNonNull(file.getContentType());
        return fileType.equals("image/png") || fileType.equals("image/jpg") || fileType.equals("image/jpeg");
    }

    /**
     * 디렉토리에 파일 저장
     *
     * @param profile 파일경로, 디렉토리경로 추출을 위한 파라미터
     * @throws IOException 파일 디렉토리 저장시 애러
     */
    public void saveFileToDirectory(MultipartFile profile, String fileName) throws IOException {
        Path path = Paths.get(FILE_PATH).toAbsolutePath().normalize();
        Path targetPath = path.resolve(fileName).normalize();

        profile.transferTo(targetPath);
    }

    /**
     * 디렉토리에 파일 삭제
     * 해당 파일 경로에 파일이 존재하면 삭제
     *
     * @param path 파일 경로
     */
    public void deleteFileToDirectory(String path) {
        File file = new File(FILE_PATH + "\\" + path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * file DB 저장
     *
     * @param profile 저장할 profile
     * @param userId profile 저장할 user Id
     * @throws IOException 파일 저장 애러시
     */
    public void save(MultipartFile profile, Long userId) throws IOException {
        fileName = getUUIDPath(profile.getOriginalFilename());

        Profile saveProfileData = ProfileRequest.create(fileName, userId);
        profileRepository.save(saveProfileData);
        saveFileToDirectory(profile, fileName);
    }


    /**
     * profile update
     * 디렉토리 저장, 기존 디렉토리 파일 삭제, DB 업데이트
     *
     * @param profile 업데이트할 profile data
     * @param authToken profile id 추출을 위한 파라미터
     * @throws Exception 일치하는 데이터가 없을 경우 Exception 발생
     */
    @Transactional
    public void updateProfile(MultipartFile profile, String authToken) throws Exception {
        Long profileId = getProfileId(authToken);
        fileName = getUUIDPath(profile.getOriginalFilename());

        saveFileToDirectory(profile, fileName);

        Profile userProfile = getProfile(profileId);
        deleteFileToDirectory(userProfile.getPath());

        update(profileId);
    }

    /**
     * DB profile data update
     * profile id 에 해당하는 data update
     *
     * @param profileId profile id 비교를 위한 파라미터
     */
    @CachePut(value = "profile", key = "#profileId", cacheManager = "projectCacheManager")
    public void update(Long profileId) {
        profileRepository.updateProfile(profileId, fileName);
    }

    /**
     * DB profile data withdrawal
     * profile id 에 해당하는 data withdrawal
     *
     * @param profileId profile id 비교를 위한 파라미터
     */
    @CacheEvict(value = "profile", key = "#profileId", cacheManager = "projectCacheManager")
    public void withdrawal(Long profileId) {
        profileRepository.deleteById(profileId);
    }

    /**
     * randomUUID 로 fileName 생성
     *
     * @param fileName fileName
     * @return random fileName
     */
    public String getUUIDPath(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IOException("Empty fileName");
        }

        String uuid = UUID.randomUUID().toString();

        return uuid + "_" + fileName;
    }
}
