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
import com.example.deukgeun.trainer.request.UpdateInfoRequest;
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

  /**
   * 트레이너 유저 저장
   *
   * @param request input data
   * @return save user
   */
  public User save(JoinRequest request) {
    User user = JoinRequest.create(request, passwordEncoder);

    return userRepository.save(user);
  }

  /**
   * 이메일에 해당하는 유저 데이터 가져오기
   *
   * @param email 이메일
   * @return findByEmail User data
   * @throws Exception When you can't find it
   */
  public User getUserByEmail(String email) throws Exception {
    return userRepository.findByEmail(email)
            .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
  }

  /**
   * authToken 으로 유저 데이터 가져오기
   * 
   * @param authToken jwt
   * @return find User data
   * @throws Exception When you can't find it
   */
  public User getUserByAuthToken(String authToken) throws Exception {
    String email = jwtService.getUserPk(authToken);
    
    return getUserByEmail(email);
  }

  /**
   * 트레이너 정보 수정
   *
   * @param request 수정할 정보
   */
  public void updateInfo(UpdateInfoRequest request) {
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

  /**
   * 트레이너 유저 비밀번호 수정
   *
   * @param request 수정할 데이터
   */
  public void updatePassword(UpdatePasswordRequest request) {
    String email = request.getEmail();
    String password = passwordEncoder.encode(request.getNewPassword());

    userRepository.updatePassword(email, password);
  }

  /**
   * 트레이너 유저 삭제
   *
   * @param id 삭제할 ID
   */
  public void withdrawal(Long id) {
    userRepository.deleteById(id);
  }

  /**
   * authToken 으로 트레이너 유저 ID 가져오기
   *
   * @param authToken jwt
   * @return user Id
   * @throws Exception When you can't find it
   */
  public Long getUserId(String authToken) throws Exception {
    User user = getUserByAuthToken(authToken);
    
    return user.getId();
  }

  /**
   * 이메일 중복 유효성 검사
   *
   * @param email 중복 확인할 email
   * @return duplicate result
   */
  public boolean isDuplicateEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  /**
   * 비밀번호 확인 유효성 검사
   *
   * @param password 비밀번호
   * @param confirm 비밀번호 확인
   * @return confirmation result
   */
  public boolean isPasswordConfirmation(String password, String confirm) {
    return password.equals(confirm);
  }

  /**
   * 빈 그룹이름 유효성 검사
   * groupStatus 가 Y 일때 groupName 에 빈 값 유효성 검사
   *
   * @param groupName 소속이름
   * @param groupStatus 소속여부
   * @return EmptyGroupName result
   */
  public boolean isEmptyGroupName(String groupName, String groupStatus) {
    return !groupStatus.equals("Y") || !groupName.isEmpty();
  }

}
