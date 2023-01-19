package com.example.deukgeun.trainer.request;



import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {
	private String name;
	
	private String email;
	 
	private String password;
	
	private String profileImage;

	private GroupStatus groupStatus;

	private String groupName;

	@Builder
	public UserRequest(String name, String email, String password, String profileImage, GroupStatus groupStatus, String groupName) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.profileImage = profileImage;
		this.groupStatus = groupStatus;
		this.groupName = groupName;
	}
	
	public static User create(UserRequest userRequest) {
		return User.builder()
				.name(userRequest.getName())
				.email(userRequest.getEmail())
				.password(userRequest.getPassword())
				.profileImage(userRequest.getProfileImage())
				.groupStatus(userRequest.getGroupStatus())
				.groupName(userRequest.getGroupName())
				.build();
				
	
	}

}
