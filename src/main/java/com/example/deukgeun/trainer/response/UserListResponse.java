package com.example.deukgeun.trainer.response;


import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
@Builder
@Getter
public class UserListResponse {
    private Long id;

    private String name;
       
    private GroupStatus groupStatus;
    
    private String groupName;
    
    public static UserListResponse fromEntity(User trainerUser) {
    	return UserListResponse.builder()
    			.id(trainerUser.getId())
    			.name(trainerUser.getName())
    			.groupName(trainerUser.getGroupName())
    			.groupStatus(trainerUser.getGroupStatus())
    			.build();
    }
}
