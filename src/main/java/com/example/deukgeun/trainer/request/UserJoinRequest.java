package com.example.deukgeun.trainer.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.commom.validator.ValidEnum;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserJoinRequest {
	
	@NotBlank(message = "이름 필수 입력 값입니다.")
	@Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z-_]{2,10}$", message = "이름은 한글 2~10자리여야 합니다.")
	private String name;
	
	@NotBlank(message = "이메일 필수 입력 값입니다.")
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
	private String email;
	
	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
	private String password;
	
	@NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호 확인은 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
	private String passwordConfirm;
	
	private String postcode;
	
	private String roadAddress;
	
	private String detailAddress;
	
	private String extraAddress;
	
	@NotNull(message = "PT 가격은 필수 입력 값입니다.")
	private int price;
	
	@ValidEnum(enumClass = Gender.class, message = "잘못된 성별 값입니다.")
	private Gender gender;
	
	@ValidEnum(enumClass = GroupStatus.class, message = "잘못된 소속 값입니다.")
	private GroupStatus groupStatus;

	private String groupName;
	
	public static User create(UserJoinRequest request, PasswordEncoder passwordEncoder) {
		return User.builder()
				.name(request.getName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.groupStatus(request.getGroupStatus())
				.groupName(request.getGroupName())
				.build();
	}
	
}
