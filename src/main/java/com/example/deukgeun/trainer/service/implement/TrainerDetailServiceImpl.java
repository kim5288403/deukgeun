package com.example.deukgeun.trainer.service.implement;

import com.example.deukgeun.trainer.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerDetailServiceImpl implements UserDetailsService{
  private final TrainerRepository trainerRepository;

  /**
   * 지정된 이메일을 사용하여 사용자 정보를 로드합니다.
   *
   * @param email 사용자 이메일
   * @return 사용자 정보
   * @throws UsernameNotFoundException 이메일에 해당하는 사용자를 찾을 수 없는 경우 예외 발생
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return trainerRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
  }
}