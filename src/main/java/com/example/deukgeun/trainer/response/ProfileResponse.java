package com.example.deukgeun.trainer.response;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponse {
  private String path;
  
  public ProfileResponse (Profile profile) {
    this.path = profile.getPath();
  }
  
  @Data
  public static class ProfileAndUserResponse {
    private String path;
    
    private String name;
    
    private String address;
    
    private Integer price;
    
    private String gender;

    private String groupName;

    public ProfileAndUserResponse (Profile profile) {
      this.path = profile.getPath();
      this.name = profile.getMember().getName();
      this.price = profile.getMember().getPrice();
      this.address = profile.getMember().getJibunAddress() + profile.getMember().getDetailAddress() + profile.getMember().getRoadAddress();
      this.gender = profile.getMember().getGender() == Gender.M ? "남" : "여";
      this.groupName = profile.getMember().getGroupName();
    }
  }
}
