package com.example.deukgeun.trainer.request;



import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequest {
  private String name;

  private String email;

  private String password;

  private String profileImage;

  private GroupStatus groupStatus;

  private String groupName;

  @Builder
  public UserRequest(String name, String email, String password, String profileImage,
      GroupStatus groupStatus, String groupName) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.profileImage = profileImage;
    this.groupStatus = groupStatus;
    this.groupName = groupName;
  }

  public static User create(UserRequest request) {
    return User.builder().name(request.getName()).email(request.getEmail())
        .password(request.getPassword()).groupStatus(request.getGroupStatus())
        .groupName(request.getGroupName()).build();
  }

}
