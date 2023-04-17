package com.example.deukgeun.trainer.service.implement;


import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.UserInfoUpdateRequest;
import com.example.deukgeun.trainer.response.UserListResponse;
import com.example.deukgeun.trainer.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

  private final UserRepository userRepository;
  
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
  
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
  }
  
  public void updatePassword(String email, String password) {
     userRepository.updatePassword(email, password);
  }
  
  public void withdrawal(User entity) {
    userRepository.delete(entity);
  }
  
  public void saveLicence() {
//    webClient.get()
//      .uri(uriBuilder -> uriBuilder
//          .path(String.format("/data.kca.kr/api/v1/cq/certificate/check"))
//          .queryParam("apiKey", "9f3a63e32b74938b30c507160e9d3b4a646f7536ccbecfbeb36ed73db90a3242")
//          .queryParam("name", "홍길동")
//          .queryParam("no", "1230001k018p")
//          .build())
//      .retrieve();
    
  }
}
