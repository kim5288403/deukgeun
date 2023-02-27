package com.example.deukgeun.trainer.response;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserResponse {
  private String email;
  
  private String name;

  private GroupStatus groupStatus;

  private Gender gender;

  private String groupName;
  
  private Profile profile;
  
  private String postcode;
  
  private String jibunAddress;
  
  private String roadAddress;
  
  private String detailAddress;
  
  private String extraAddress;
  
  private Integer price; 
  
  public UserResponse (User user) {
    this.email = user.getEmail();
    this.name = user.getName();
    this.groupStatus = user.getGroupStatus();
    this.groupName = user.getGroupName();
    this.profile = user.getProfile();
    this.postcode = user.getPostcode();
    this.jibunAddress = user.getJibunAddress();
    this.roadAddress = user.getRoadAddress();
    this.detailAddress = user.getDetailAddress();
    this.extraAddress = user.getExtraAddress();
    this.gender = user.getGender();
    this.price = user.getPrice();
  }
}
