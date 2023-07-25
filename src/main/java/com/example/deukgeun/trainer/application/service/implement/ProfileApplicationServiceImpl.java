package com.example.deukgeun.trainer.application.service.implement;

import com.example.deukgeun.trainer.application.service.ProfileApplicationService;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.service.ProfileDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProfileApplicationServiceImpl implements ProfileApplicationService {

    private final ProfileDomainService profileDomainService;

    @Value("${trainer.profile.filePath}")
    private String FILE_PATH;

    private String fileName;

    @CacheEvict(value = "profile", key = "#profileId", cacheManager = "projectCacheManager")
    public void deleteById(Long profileId) {
        profileDomainService.deleteById(profileId);
    }

    public void deleteFileToDirectory(String fileName) throws IOException {
        Path directory = Path.of(FILE_PATH);

        try (Stream<Path> pathStream = Files.find(directory, Integer.MAX_VALUE,
                (path, attributes) -> path.getFileName().toString().equals(fileName))) {
            pathStream.forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public String getUUIDPath(String fileName) throws IOException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IOException("Empty fileName");
        }

        String uuid = UUID.randomUUID().toString();

        return uuid + "_" + fileName;
    }

    @Cacheable(value = "profile", key = "#profileId", cacheManager = "projectCacheManager", unless = "#result == null")
    public Profile findById(Long profileId) {
        return profileDomainService.findById(profileId);
    }

    public Profile findByTrainerId(Long trainerId) {
        return profileDomainService.findByTrainerId(trainerId);
    }

    public boolean isSupportedContentType(MultipartFile file) {
        String fileType = Objects.requireNonNull(file.getContentType());
        return fileType.equals("image/png") || fileType.equals("image/jpg") || fileType.equals("image/jpeg");
    }

    public void save(MultipartFile profile, Long trainerId) throws IOException {
        fileName = getUUIDPath(profile.getOriginalFilename());

        profileDomainService.save(fileName, trainerId);
        saveFileToDirectory(profile, fileName);
    }

    public void saveFileToDirectory(MultipartFile profile, String fileName) throws IOException {
        Path path = Paths.get(FILE_PATH).toAbsolutePath().normalize();
        Path targetPath = path.resolve(fileName).normalize();

        profile.transferTo(targetPath);
    }

    @CachePut(value = "profile", key = "#profileId", cacheManager = "projectCacheManager", unless="#result == null")
    public void update(Profile profile, String path) {
        profileDomainService.update(profile, path);
    }

    @Transactional
    public void updateProfile(MultipartFile profile, Long profileId) throws Exception {
        fileName = getUUIDPath(profile.getOriginalFilename());

        saveFileToDirectory(profile, fileName);

        Profile userProfile = findById(profileId);
        deleteFileToDirectory(userProfile.getPath());

        update(userProfile, fileName);
    }
}
