package com.example.deukgeun.trainer.service.implement;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.deukgeun.commom.util.WebClientUtil;
import com.example.deukgeun.trainer.entity.License;
import com.example.deukgeun.trainer.repository.LicenseRepository;
import com.example.deukgeun.trainer.request.SaveLicenseRequest;
import com.example.deukgeun.trainer.response.LicenseListResponse;
import com.example.deukgeun.trainer.response.LicenseResultResponse;
import com.example.deukgeun.trainer.service.LicenseService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LicenseServiceImpl implements LicenseService{
   
  private final UserServiceImpl userService;
  private final LicenseRepository licenseRepository;
  
  @Value("${trainer.license.api.key}")
  private String licenseApiKey;
  @Value("${trainer.license.api.uri}")
  private String licenseApiUri;

  @Cacheable(value = "license", key = "#userId", cacheManager = "projectCacheManager")
  public List<LicenseListResponse> findByUserId(Long userId) {
    return licenseRepository.findByUserId(userId);
  }

  public void save(SaveLicenseRequest request, String authToken) throws Exception {
    LicenseResultResponse licenseResult = checkLicense(request);
    
    if (licenseResult.getResult()) {
      Long userId = userService.getUserId(authToken);
      
      License license = SaveLicenseRequest.create(licenseResult.getCertificatename(), request.getNo(), userId);
      licenseRepository.save(license);
      
    } else {
      throw new Exception("존재하지않는 자격증 정보 입니다.");
    }
  }

  @CacheEvict(value = "license", key = "#id", cacheManager = "projectCacheManager")
  public void remove(Long id) {
    licenseRepository.deleteById(id);
  }
  
  public LicenseResultResponse checkLicense(SaveLicenseRequest request) {
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
