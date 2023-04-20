package com.example.deukgeun.trainer.response;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
  private String email;
  
  private String name;

  private GroupStatus groupStatus;

  private Gender gender;

  private String groupName;
  
  private String postcode;
  
  private String jibunAddress;
  
  private String roadAddress;
  
  private String detailAddress;
  
  private String extraAddress;
  
  private String introduction;
  
  private Integer price;
  
  public UserResponse (User user) {
    this.email = user.getEmail();
    this.name = user.getName();
    this.groupStatus = user.getGroupStatus();
    this.groupName = user.getGroupName();
    this.postcode = user.getPostcode();
    this.jibunAddress = user.getJibunAddress();
    this.roadAddress = user.getRoadAddress();
    this.detailAddress = user.getDetailAddress();
    this.extraAddress = user.getExtraAddress();
    this.gender = user.getGender();
    this.price = user.getPrice();
    this.introduction = user.getIntroduction();
  }
  
  @Data
  @AllArgsConstructor
  public static class UserListResponse {

    private Long id;

    private String name;

    private String path;

    private GroupStatus groupStatus;

    private String groupName;
    
    private String introduction;
    
    public UserListResponse(Profile profile) {
      this.id = profile.getUserId();
      this.path = profile.getPath();
      this.name = profile.getUser().getName();
      this.groupStatus = profile.getUser().getGroupStatus();
      this.groupName = profile.getUser().getGroupName();
      this.introduction = profile.getUser().getIntroduction();
    }
  }
  
}
