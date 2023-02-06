package com.example.deukgeun.trainer.response;


import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class UserListResponse {
  private Long id;

  private String name;

  private GroupStatus groupStatus;

  private String groupName;
  
  private Profile profile;
}
