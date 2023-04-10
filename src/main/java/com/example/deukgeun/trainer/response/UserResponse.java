package com.example.deukgeun.trainer.response;

import org.apache.commons.text.StringEscapeUtils;
import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.Post;
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
  
  private String postcode;
  
  private String jibunAddress;
  
  private String roadAddress;
  
  private String detailAddress;
  
  private String extraAddress;
  
  private String introduction;
  
  private Integer price;
  
  private Profile profile;
  
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
    this.profile = user.getProfile();
  }
  
  @Data
  @AllArgsConstructor
  public static class UserAndProfile {
    private String email;
    
    private String name;

    private Gender gender;

    private String groupName;
    
    private String jibunAddress;
    
    private String roadAddress;
    
    private String detailAddress;
    
    private String extraAddress;
    
    private Integer price;
    
    private String path;
    
    public UserAndProfile (User user) {
      this.email = user.getEmail();
      this.name = user.getName();
      this.groupName = user.getGroupName();
      this.jibunAddress = user.getJibunAddress();
      this.roadAddress = user.getRoadAddress();
      this.detailAddress = user.getDetailAddress();
      this.extraAddress = user.getExtraAddress();
      this.gender = user.getGender();
      this.price = user.getPrice();
      this.path = user.getProfile().getPath();
    }
  }
  
  @Data
  @AllArgsConstructor
  public static class UserDetail {
    private Long id;
    
    private String name;

    private String gender;

    private String groupName;
    
    private String address;
    
    private Integer price;
    
    private String path;

    private String html;
    
    public UserDetail (User user, Post post) {
      this.id = user.getId();
      this.name = user.getName();
      this.groupName = user.getGroupName();
      this.address = user.getJibunAddress() + " " + user.getRoadAddress() + " " +  user.getDetailAddress() + " " + user.getExtraAddress();
      this.gender = user.getGender() == Gender.M ? "남" : "여";
      this.price = user.getPrice();
      this.path = user.getProfile().getPath();
      this.html = StringEscapeUtils.unescapeHtml3(post.getHtml());
    }
  }
  
  
}
