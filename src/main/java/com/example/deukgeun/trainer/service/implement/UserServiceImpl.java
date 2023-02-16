package com.example.deukgeun.trainer.service.implement;


import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.response.UserListResponse;
import com.example.deukgeun.trainer.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService{

	private final UserRepository userRepository;

	public List<UserListResponse> getList(String keyword) {
		return userRepository.findByNameOrGroupName(keyword, keyword); 
	}

	public Long save(User user) {
		User res = userRepository.save(user);
		return res.getId();
	}

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return (UserDetails) userRepository.findByEmail(email)
    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
  }

}
