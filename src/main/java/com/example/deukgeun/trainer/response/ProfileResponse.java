package com.example.deukgeun.trainer.response;

import java.util.List;
import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.License;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.User;
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
      this.name = profile.getUser().getName();
      this.price = profile.getUser().getPrice();
      this.address = profile.getUser().getJibunAddress() + profile.getUser().getDetailAddress() + profile.getUser().getRoadAddress();
      this.gender = profile.getUser().getGender() == Gender.M ? "남" : "여";
      this.groupName = profile.getUser().getGroupName();
    }
  }
}
