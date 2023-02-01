package com.example.deukgeun.trainer.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.repository.ProfileRepository;
import com.example.deukgeun.trainer.service.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService {
	
	@Autowired
	private ProfileRepository profileRepository;
	
	public BindingResult validator(MultipartFile file, BindingResult bindingResult) {
		if (file.isEmpty()) {
			bindingResult.addError(new FieldError("profile", "profile", "프로필 이미지는 필수입니다."));
		}
		
		if (!isSupportedContentType(file.getContentType())) {
			bindingResult.addError(new FieldError("profile", "profile", "이미지 파일만 업로드 가능합니다."));
		}
		
		return bindingResult;
	}
	
	private boolean isSupportedContentType(String fileType) {
		return fileType.equals("image/png")
				|| fileType.equals("image/jpg")
				|| fileType.equals("image/jpeg");
	}
	
	public void save(Profile profile) {
		profileRepository.save(profile);
	}
	
}
