package com.example.deukgeun.trainer.application.dto.response;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.Profile;
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
      this.name = profile.getTrainer().getName();
      this.price = profile.getTrainer().getPrice();
      this.address = profile.getTrainer().getJibunAddress() + profile.getTrainer().getDetailAddress() + profile.getTrainer().getRoadAddress();
      this.gender = profile.getTrainer().getGender() == Gender.M ? "남" : "여";
      this.groupName = profile.getTrainer().getGroupName();
    }
  }
}
