package com.example.deukgeun.trainer.service.implement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.repository.ProfileRepository;
import com.example.deukgeun.trainer.request.ProfileRequest;
import com.example.deukgeun.trainer.service.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService {

  @Autowired
  private ProfileRepository profileRepository;
  
  //트레이너 profile 저장 경로
  private static final String FILE_PATH = "C:\\eclipse\\deukgeun_workspace\\deukgeun\\src\\main\\resources\\static\\images\\trainer\\profile";

  //file custom validate 
  public BindingResult validator(MultipartFile file, BindingResult bindingResult) {
    if(file == null) {
      bindingResult.addError(new FieldError("profile", "profile", "프로필 이미지는 필수입니다."));
    } else {
      
      if (file.isEmpty()) {
        bindingResult.addError(new FieldError("profile", "profile", "프로필 이미지는 필수입니다."));
      }

      if (!isSupportedContentType(file.getContentType())) {
        bindingResult.addError(new FieldError("profile", "profile", "이미지 파일만 업로드 가능합니다."));
      }
      
    }
    
    return bindingResult;
  }
  
  public Profile getProfile(Long profileId) {
    return profileRepository.getById(profileId);
  }
  
  //file type 비교 
  private boolean isSupportedContentType(String fileType) {
    return fileType.equals("image/png") || fileType.equals("image/jpg")
        || fileType.equals("image/jpeg");
  }
  
  //server에 file 저장
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

  public Long save(MultipartFile profile) {
    UUID uuid = UUID.randomUUID();
    String path = uuid.toString() + "_" + profile.getOriginalFilename();
    
    ProfileRequest profileRequest = ProfileRequest
        .builder()
        .path(path)
        .build();
    
    Profile saveProfileData = ProfileRequest.create(profileRequest);
    Profile res = profileRepository.save(saveProfileData);
    saveServer(profile, path);
    
    return res.getId();
  }
  
  public void updateProfile(Long profileId, String path) {
    profileRepository.updateProfile(profileId, path);
  }

}
