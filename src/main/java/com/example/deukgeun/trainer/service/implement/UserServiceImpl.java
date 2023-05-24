package com.example.deukgeun.trainer.service.implement;

import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.request.LoginRequest;
import com.example.deukgeun.trainer.request.UpdatePasswordRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.ProfileRepository;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.UpdateUserRequest;
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

  public void login(LoginRequest request) throws Exception {
    String email = request.getEmail();
    String password = request.getPassword();

    User user = getUserByEmail(email);

    boolean check = passwordEncoder.matches(password, user.getPassword());

    if (!check) {
      throw new Exception("사용자를 찾을 수 없습니다.");
    }
  }

  /**
   * 트레이너 리스트 조건 검색
   *
   * @param keyword 검색어
   * @param currentPage 선택 페이지
   * @return 검색 결과
   */
  public Page<UserListResponse> getList(String keyword, Integer currentPage) {
    String likeKeyword = "%" + keyword + "%";
    PageRequest pageable = PageRequest.of(currentPage, 10);
    return profileRepository.findByUserLikeKeyword(likeKeyword, pageable);
  }

  public User save(JoinRequest request) {
    User user = JoinRequest.create(request, passwordEncoder);

    return userRepository.save(user);
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
  
  public void updateInfo(UpdateUserRequest request) {
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
  
  public void updatePassword(UpdatePasswordRequest request) {
    String email = request.getEmail();
    String password = passwordEncoder.encode(request.getNewPassword());

    userRepository.updatePassword(email, password);
  }

  public void withdrawal(Long id) {
    userRepository.deleteById(id);
  }

  public Long getUserId(String authToken) throws Exception {
    User user = getUserByAuthToken(authToken);
    
    return user.getId();
  }

  public boolean isDuplicateEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public boolean isPasswordConfirmation(String password, String confirm) {
    return password.equals(confirm);
  }

  public boolean isEmptyGroupName(String groupName, String groupStatus) {
    return !groupStatus.equals("Y") || !groupName.isEmpty();
  }

}
