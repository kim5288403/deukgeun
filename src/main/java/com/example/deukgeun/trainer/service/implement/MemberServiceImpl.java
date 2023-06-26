package com.example.deukgeun.trainer.service.implement;

import com.example.deukgeun.commom.exception.PasswordMismatchException;
import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.trainer.entity.Member;
import com.example.deukgeun.trainer.repository.MemberRepository;
import com.example.deukgeun.trainer.repository.ProfileRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.request.LoginRequest;
import com.example.deukgeun.trainer.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.response.MemberResponse;
import com.example.deukgeun.trainer.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final ProfileRepository profileRepository;
  private final TokenServiceImpl tokenService;
  private final PasswordEncoder passwordEncoder;

  /**
   * 인증을 위해 제공된 비밀번호가 사용자의 비밀번호와 일치하는지 확인합니다.
   *
   * @param request 이메일과 비밀번호가 포함된 로그인 요청 객체
   * @throws EntityNotFoundException   사용자가 찾을 수 없는 경우 예외가 발생합니다.
   * @throws PasswordMismatchException 비밀번호가 사용자의 비밀번호와 일치하지 않는 경우 예외가 발생합니다.
   */
  public void isPasswordMatches(LoginRequest request) throws EntityNotFoundException {
    String email = request.getEmail();
    String password = request.getPassword();

    Member member = getByEmail(email);

    boolean check = passwordEncoder.matches(password, member.getPassword());

    if (!check) {
      throw new PasswordMismatchException("사용자를 찾을 수 없습니다.");
    }
  }

  /**
   * 주어진 키워드와 현재 페이지를 기반으로 사용자 목록을 조회합니다.
   *
   * @param keyword      사용자 목록에서 검색할 키워드
   * @param currentPage  현재 페이지 번호
   * @return 페이지로 나뉘어진 사용자 목록
   */
  public Page<MemberResponse.MemberListResponse> getList(String keyword, Integer currentPage) {
    String likeKeyword = "%" + keyword + "%";
    PageRequest pageable = PageRequest.of(currentPage, 10);
    return profileRepository.findByUserLikeKeyword(likeKeyword, pageable);
  }

  /**
   * 주어진 회원가입 요청을 바탕으로 사용자를 저장합니다.
   *
   * @param request 회원가입 요청 객체
   * @return 저장된 사용자
   */
  public Member save(JoinRequest request) {
    Member member = Member
            .builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .groupStatus(request.getGroupStatus())
            .groupName(request.getGroupName())
            .postcode(request.getPostcode())
            .jibunAddress(request.getJibunAddress())
            .roadAddress(request.getRoadAddress())
            .detailAddress(request.getDetailAddress())
            .extraAddress(request.getExtraAddress())
            .gender(request.getGender())
            .price(request.getPrice())
            .introduction(request.getIntroduction())
            .build();

    return memberRepository.save(member);
  }

  /**
   * 주어진 이메일을 기반으로 사용자를 조회합니다.
   *
   * @param email 사용자의 이메일
   * @return 조회된 사용자
   * @throws EntityNotFoundException 주어진 이메일에 해당하는 사용자가 없는 경우 발생하는 예외
   */
  public Member getByEmail(String email) throws EntityNotFoundException {
    return memberRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
  }

  /**
   * 주어진 인증 토큰을 기반으로 사용자를 조회합니다.
   *
   * @param authToken 인증 토큰
   * @return 조회된 사용자
   * @throws EntityNotFoundException 주어진 인증 토큰에 해당하는 사용자가 없는 경우 발생하는 예외
   */
  public Member getByAuthToken(String authToken) throws EntityNotFoundException {
    String email = tokenService.getUserPk(authToken);
    return getByEmail(email);
  }

  /**
   * 주어진 정보로 사용자 정보를 업데이트합니다.
   *
   * @param request 업데이트할 사용자 정보 요청 객체
   */
  public void updateInfo(UpdateInfoRequest request) {
    Member foundMember = memberRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    foundMember.updateInfo(request);
    memberRepository.save(foundMember);
  }

  /**
   * 주어진 요청에 따라 비밀번호를 업데이트합니다.
   *
   * @param request 비밀번호 업데이트 요청 객체
   */
  public void updatePassword(UpdatePasswordRequest request) {
    String email = request.getEmail();
    String newPassword = passwordEncoder.encode(request.getNewPassword());
    Member foundMember = memberRepository.findByEmail(email).orElse(null);

    assert foundMember != null;
    foundMember.updatePassword(newPassword);
    memberRepository.save(foundMember);
  }

  /**
   * 주어진 ID에 해당하는 사용자를 삭제합니다.
   *
   * @param id 삭제할 사용자의 ID
   */
  public void withdrawal(Long id) {
    memberRepository.deleteById(id);
  }

  /**
   * 주어진 인증 토큰에 해당하는 사용자의 ID를 가져옵니다.
   *
   * @param authToken 인증 토큰
   * @return 사용자의 ID
   * @throws EntityNotFoundException 사용자를 찾을 수 없는 경우 발생하는 예외
   */
  public Long getUserId(String authToken) throws EntityNotFoundException {
    Member member = getByAuthToken(authToken);
    
    return member.getId();
  }

  /**
   * 주어진 이메일이 중복되는지 확인합니다.
   *
   * @param email 확인할 이메일
   * @return 중복 여부 (true: 중복됨, false: 중복되지 않음)
   */
  public boolean isDuplicateEmail(String email) {
    return memberRepository.existsByEmail(email);
  }

  /**
   * 그룹명이 비어있는지 여부를 확인합니다.
   * 그룹 상태가 "Y"가 아니거나 그룹명이 비어있지 않은 경우에는 true 를 반환합니다.
   *
   * @param groupName   확인할 그룹명
   * @param groupStatus 그룹 상태
   * @return 그룹명이 비어있는지 여부 (true: 비어있지 않음, false: 비어있음)
   */
  public boolean isEmptyGroupName(String groupName, String groupStatus) {
    return !groupStatus.equals("Y") || !groupName.isEmpty();
  }

}
