package com.example.deukgeun.trainer.service.implement;


import java.util.List;
import org.springframework.stereotype.Service;
import com.example.deukgeun.global.provider.JwtProvider;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.ProfileRepository;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.UserInfoUpdateRequest;
import com.example.deukgeun.trainer.response.UserResponse.UserListResponse;
import com.example.deukgeun.trainer.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final ProfileRepository profileRepository;
  private final JwtProvider jwtProvider;
  
  public List<UserListResponse> getList(String keyword) {
    
    keyword = "%" + keyword + "%";
    return profileRepository.findByUserLikeKeyword(keyword);
  }

  public Long save(User user) {
    
    User res = userRepository.save(user);
    return res.getId();
  }
  
  public User findByIdUser(Long id) throws Exception {
    
    return findByIdUser(id);
  }

  public User getUser(String authToken) throws Exception {
    String email = jwtProvider.getUserPk(authToken);
    
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
  }
  
  public User login(String email) throws Exception {
    
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
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
  
  public void withdrawal(String authToken) throws Exception {
    String email = jwtProvider.getUserPk(authToken);
    User user = getUser(email);
    
    userRepository.delete(user);
  }
  
  public Long getUserId(String authToken) throws Exception {
    User user = getUser(authToken);
    
    return user.getId();
  }
}
