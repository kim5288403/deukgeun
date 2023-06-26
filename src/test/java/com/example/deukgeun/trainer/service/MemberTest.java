package com.example.deukgeun.trainer.service;

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
import com.example.deukgeun.trainer.service.implement.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MemberTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private TokenServiceImpl tokenService;
    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    public void givenMatchingPassword_whenIsPasswordMatches_thenNoExceptionThrown() throws EntityNotFoundException {
        // Given
        String email = "example@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword123";

        LoginRequest request = new LoginRequest(email, password);
        Member member = Member
                .builder()
                .email(email)
                .password(encodedPassword)
                .build();

        given(memberRepository.findByEmail(email)).willReturn(Optional.ofNullable(member));
        given(passwordEncoder.matches(password, encodedPassword)).willReturn(true);

        // When & Then
        assertDoesNotThrow(() -> memberService.isPasswordMatches(request));

        // Verify
        verify(memberRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    @Test
    public void givenMismatchingPassword_whenIsPasswordMatches_thenPasswordMismatchExceptionThrown() throws EntityNotFoundException {
        // Given
        String email = "example@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword123";

        LoginRequest request = new LoginRequest(email, password);
        Member member = Member
                .builder()
                .email(email)
                .password(encodedPassword)
                .build();

        given(memberRepository.findByEmail(email)).willReturn(Optional.ofNullable(member));
        given(passwordEncoder.matches(password, encodedPassword)).willReturn(false);

        // When & Then
        assertThrows(PasswordMismatchException.class, () -> memberService.isPasswordMatches(request));

        // Verify
        verify(memberRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    @Test
    public void givenKeywordAndPage_whenGetList_thenReturnsMatchingUsers() {
        // Given
        String keyword = "john";
        int currentPage = 0;

        String likeKeyword = "%" + keyword + "%";
        PageRequest pageable = PageRequest.of(currentPage, 10);

        MemberResponse.MemberListResponse member1 = new MemberResponse.MemberListResponse();
        member1.setId(1L);
        member1.setName("name1");
        member1.setPath("path1");

        MemberResponse.MemberListResponse member2 = new MemberResponse.MemberListResponse();
        member2.setId(2L);
        member2.setName("name2");
        member2.setPath("path2");

        List<MemberResponse.MemberListResponse> memberList = new ArrayList<>();
        memberList.add(member1);
        memberList.add(member2);
        Page<MemberResponse.MemberListResponse> userPage = new PageImpl<>(memberList, pageable, memberList.size());

        given(profileRepository.findByUserLikeKeyword(likeKeyword, pageable)).willReturn(userPage);

        // When
        Page<MemberResponse.MemberListResponse> result = memberService.getList(keyword, currentPage);

        // Then
        assertNotNull(result);
        assertEquals(memberList.size(), result.getContent().size());

        // Verify
        verify(profileRepository, times(1)).findByUserLikeKeyword(likeKeyword, pageable);
    }

    @Test
    public void givenJoinRequest_whenSave_thenMemberIsSavedAndReturned() {
        // Given
        JoinRequest request = new JoinRequest();
        request.setName("John Doe");
        request.setEmail("johndoe@example.com");
        request.setPassword("password123");

        Member savedMember = Member
                .builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();

        given(passwordEncoder.encode(request.getPassword())).willReturn("encodedPassword123");
        given(memberRepository.save(any(Member.class))).willReturn(savedMember);

        // When
        Member result = memberService.save(request);

        // Then
        assertNotNull(result);
        assertEquals(savedMember.getId(), result.getId());
        assertEquals(savedMember.getName(), result.getName());
        assertEquals(savedMember.getEmail(), result.getEmail());

        // Verify
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    public void givenExistingEmail_whenGetByEmail_thenReturnsMatchingMember() throws EntityNotFoundException {
        // Given
        String email = "johndoe@example.com";
        Member member = Member
                .builder()
                .email(email)
                .build();

        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        Member result = memberService.getByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(member.getEmail(), result.getEmail());

        // Verify
        verify(memberRepository, times(1)).findByEmail(email);
    }

    @Test
    public void givenNonexistentEmail_whenGetByEmail_thenThrowsEntityNotFoundException() {
        // Given
        String email = "nonexistent@example.com";

        given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> memberService.getByEmail(email));

        // Verify
        verify(memberRepository, times(1)).findByEmail(email);
    }

    @Test
    public void givenValidAuthToken_whenGetByAuthToken_thenReturnsMatchingMember() throws EntityNotFoundException {
        // Given
        String authToken = "validAuthToken";
        String email = "johndoe@example.com";
        Member member = Member
                .builder()
                .email(email)
                .build();

        given(tokenService.getUserPk(authToken)).willReturn(email);
        given(memberRepository.findByEmail(email)).willReturn(Optional.ofNullable(member));

        // When
        Member result = memberService.getByAuthToken(authToken);

        // Then
        assertNotNull(result);
        assertNotNull(member);
        assertEquals(member.getEmail(), result.getEmail());

        // Verify
        verify(tokenService, times(1)).getUserPk(authToken);
        verify(memberRepository, times(1)).findByEmail(email);
    }

    @Test
    public void givenInvalidAuthToken_whenGetByAuthToken_thenThrowsEntityNotFoundException() throws EntityNotFoundException {
        // Given
        String authToken = "invalidAuthToken";

        given(tokenService.getUserPk(authToken)).willReturn(null);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> memberService.getByAuthToken(authToken));

        // Verify
        verify(tokenService, times(1)).getUserPk(authToken);
    }

    @Test
    public void givenValidUpdateInfoRequest_whenUpdateInfo_thenMemberIsUpdatedAndSaved() throws EntityNotFoundException {
        // Given
        UpdateInfoRequest request = new UpdateInfoRequest();
        request.setEmail("johndoe@example.com");

        Member foundMember = Member
                .builder()
                .email(request.getEmail())
                .build();

        given(memberRepository.findByEmail(request.getEmail())).willReturn(Optional.of(foundMember));
        given(memberRepository.save(foundMember)).willReturn(foundMember);

        // When
        memberService.updateInfo(request);

        // Verify
        verify(memberRepository, times(1)).findByEmail(request.getEmail());
        verify(memberRepository, times(1)).save(foundMember);
    }

    @Test
    public void givenInvalidUpdateInfoRequest_whenUpdateInfo_thenThrowsEntityNotFoundException() {
        // Given
        UpdateInfoRequest request = new UpdateInfoRequest();
        request.setEmail("nonexistent@example.com");

        given(memberRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> memberService.updateInfo(request));

        // Verify
        verify(memberRepository, times(1)).findByEmail(request.getEmail());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    public void givenValidUpdatePasswordRequest_whenUpdatePassword_thenPasswordIsUpdatedAndSaved() {
        // Given
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setEmail("johndoe@example.com");
        request.setNewPassword("newPassword123");

        Member foundMember = Member
                .builder()
                .email(request.getEmail())
                .build();

        String encodedNewPassword = "encodedNewPassword123";

        given(memberRepository.findByEmail(request.getEmail())).willReturn(Optional.of(foundMember));
        given(passwordEncoder.encode(request.getNewPassword())).willReturn(encodedNewPassword);
        given(memberRepository.save(foundMember)).willReturn(foundMember);

        // When
        memberService.updatePassword(request);

        // Then
        assertEquals(encodedNewPassword, foundMember.getPassword());

        // Verify
        verify(memberRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(request.getNewPassword());
        verify(memberRepository, times(1)).save(foundMember);
    }

    @Test
    public void givenInvalidUpdatePasswordRequest_whenUpdatePassword_thenMemberIsNotFound() {
        // Given
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        String encodedNewPassword = "encodedNewPassword123";
        request.setEmail("nonexistent@example.com");
        request.setNewPassword("newPassword123");

        given(memberRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());
        given(passwordEncoder.encode(request.getNewPassword())).willReturn(encodedNewPassword);

        // When & Then
        assertThrows(AssertionError.class, () -> memberService.updatePassword(request));

        // Verify
        verify(memberRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(request.getNewPassword());
    }

    @Test
    public void givenExistingMemberId_whenWithdrawal_thenMemberIsDeleted() {
        // Given
        Long memberId = 123L;

        // When
        memberService.withdrawal(memberId);

        // Verify
        verify(memberRepository, times(1)).deleteById(memberId);
    }

    @Test
    public void givenValidAuthToken_whenGetUserId_thenReturnUserId() throws EntityNotFoundException {
        // Given
        String authToken = "validAuthToken";
        String email = "johndoe@example.com";
        Long userId = 123L;
        Member member = Member
                .builder()
                .id(userId)
                .email(email)
                .build();

        given(tokenService.getUserPk(authToken)).willReturn(email);
        given(memberRepository.findByEmail(email)).willReturn(Optional.ofNullable(member));

        // When
        Long result = memberService.getUserId(authToken);

        // Then
        assertEquals(userId, result);

        // Verify
        verify(tokenService, times(1)).getUserPk(authToken);
        verify(memberRepository, times(1)).findByEmail(email);
    }

    @Test
    public void givenExistingEmail_whenIsDuplicateEmail_thenReturnTrue() {
        // Given
        String email = "johndoe@example.com";

        given(memberRepository.existsByEmail(email)).willReturn(true);

        // When
        boolean result = memberService.isDuplicateEmail(email);

        // Then
        assertTrue(result);

        // Verify
        verify(memberRepository, times(1)).existsByEmail(email);
    }

    @Test
    public void givenNonExistingEmail_whenIsDuplicateEmail_thenReturnFalse() {
        // Given
        String email = "nonexistent@example.com";

        given(memberRepository.existsByEmail(email)).willReturn(false);

        // When
        boolean result = memberService.isDuplicateEmail(email);

        // Then
        assertFalse(result);

        // Verify
        verify(memberRepository, times(1)).existsByEmail(email);
    }

    @Test
    public void givenValidGroupStatusAndNonEmptyGroupName_whenIsEmptyGroupName_thenReturnFalse() {
        // Given
        String groupName = "Group A";
        String groupStatus = "Y";

        // When
        boolean result = memberService.isEmptyGroupName(groupName, groupStatus);

        // Then
        assertTrue(result);
    }

    @Test
    public void givenValidGroupStatusAndEmptyGroupName_whenIsEmptyGroupName_thenReturnTrue() {
        // Given
        String groupName = "";
        String groupStatus = "Y";

        // When
        boolean result = memberService.isEmptyGroupName(groupName, groupStatus);

        // Then
        assertFalse(result);
    }

    @Test
    public void givenInvalidGroupStatusAndNonEmptyGroupName_whenIsEmptyGroupName_thenReturnTrue() {
        // Given
        String groupName = "Group A";
        String groupStatus = "N";

        // When
        boolean result = memberService.isEmptyGroupName(groupName, groupStatus);

        // Then
        assertTrue(result);
    }

    @Test
    public void givenInvalidGroupStatusAndEmptyGroupName_whenIsEmptyGroupName_thenReturnTrue() {
        // Given
        String groupName = "";
        String groupStatus = "N";

        // When
        boolean result = memberService.isEmptyGroupName(groupName, groupStatus);

        // Then
        assertTrue(result);
    }
}
