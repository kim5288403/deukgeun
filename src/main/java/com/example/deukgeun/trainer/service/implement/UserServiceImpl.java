package com.example.deukgeun.trainer.service.implement;


import java.util.List;

import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.request.PasswordUpdateRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.ProfileRepository;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.UserUpdateRequest;
import com.example.deukgeun.trainer.response.UserResponse.UserListResponse;
import com.example.deukgeun.trainer.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final ProfileRepository profileRepository;
  private final JwtServiceImpl jwtService;
  private final PasswordEncoder passwordEncoder;

  @Cacheable(value = "getUserList", key = "#keyword", cacheManager = "projectCacheManager")
  public List<UserListResponse> getList(String keyword) {
    
    keyword = "%" + keyword + "%";
    return profileRepository.findByUserLikeKeyword(keyword);
  }

  public Long save(JoinRequest request) {
    User user = JoinRequest.create(request, passwordEncoder);
    User res = userRepository.save(user);

    return res.getId();
  }

  public User getUserByEmail(String email) throws Exception {
    return userRepository.findByEmail(email)
            .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
  }

  public User getUserByAuthToken(String authToken) throws Exception {
    String email = jwtService.getUserPk(authToken);
    
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
  }
  
  public User login(String email) throws Exception {
    
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
  }
  
  public void updateInfo(UserUpdateRequest request) {
    
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
  
  public void updatePassword(PasswordUpdateRequest request) {
    String email = request.getEmail();
    String password = passwordEncoder.encode(request.getNewPassword());

    userRepository.updatePassword(email, password);
  }
  
  public void withdrawal(String authToken) throws Exception {
    User user = getUserByAuthToken(authToken);
    
    userRepository.delete(user);
  }
  
  public Long getUserId(String authToken) throws Exception {
    User user = getUserByAuthToken(authToken);
    
    return user.getId();
  }
}
