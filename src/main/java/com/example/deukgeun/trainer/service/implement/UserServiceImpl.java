package com.example.deukgeun.trainer.service.implement;


import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.deukgeun.commom.repository.LicenseRepository;
import com.example.deukgeun.commom.util.WebClientUtil;
import com.example.deukgeun.global.provider.JwtProvider;
import com.example.deukgeun.trainer.entity.License;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.request.UserInfoUpdateRequest;
import com.example.deukgeun.trainer.response.LicenseResultResponse;
import com.example.deukgeun.trainer.response.UserListResponse;
import com.example.deukgeun.trainer.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final LicenseRepository licenseRepository;
  private final JwtProvider jwtProvider;
  
  @Value("${trainer.license.api.key}")
  private String licenseApiKey;
  @Value("${trainer.license.api.uri}")
  private String licenseApiUri;
  
  public List<UserListResponse> getList(String keyword) {
    keyword = "%" + keyword + "%";
    return userRepository.findByNameLikeOrGroupNameLikeOrJibunAddressLikeOrRoadAddressLikeOrDetailAddressLikeOrExtraAddressLike(
        keyword,
        keyword,
        keyword,
        keyword,
        keyword,
        keyword);
  }

  public Long save(User user) {
    User res = userRepository.save(user);
    return res.getId();
  }
  
  public User findByIdUser(Long id) throws Exception {
    return userRepository.findById(id)
        .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
    
  }

  public User getUser(String email) throws Exception {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
  }
  
  public Long getProfileId(String email) throws Exception {
    User user = getUser(email);
    return user.getProfileId();
  }
  
  public void updateInfo(UserInfoUpdateRequest request) {
    userRepository.updateInfo(
        request.getEmail(),
        request.getName(),
        request.getGender(),
        request.getPostcode(),
        request.getJibunAddress(),
        request.getRoadAddress(),
        request.getDetailAddress(),
        request.getExtraAddress(),
        request.getPrice(),
        request.getGroupStatus(),
        request.getGroupName(),
        request.getIntroduction()
        );
  }
  
  public void updatePassword(String email, String password) {
     userRepository.updatePassword(email, password);
  }
  
  public void withdrawal(User entity) {
    userRepository.delete(entity);
  }
  
  public void saveLicence(SaveLicenseRequest request, String authToken) throws Exception {
    LicenseResultResponse licenseResult = checkLicence(request);
    
    if (licenseResult.getResult()) {
      String email = jwtProvider.getUserPk(authToken);
      User user = getUser(email);
      
      License license = SaveLicenseRequest.create(licenseResult.getCertificatename(), user.getId());
      licenseRepository.save(license);
      
    } else {
      throw new Exception("존재하지않는 자격증 정보 입니다.");
    }
  }
  
  public LicenseResultResponse checkLicence(SaveLicenseRequest request) {
    WebClient webClient = WebClientUtil.getBaseUrl(licenseApiUri);
    
    return webClient.get().
        uri(uriBuilder -> uriBuilder
        .path("")
        .queryParam("apiKey", licenseApiKey)
        .queryParam("name", request.getName())
        .queryParam("no", request.getNo())
        .build())
  .retrieve() 
    .bodyToMono(LicenseResultResponse.class)
    .block();
    
  }
}
