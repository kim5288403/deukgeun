package com.example.deukgeun.trainer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponse {
  private String path;
  
  @Data
  public static class ProfileAndUserResponse {
    private String path;
    
    private String name;
    
    private String address;
    
    private Integer price;
    
    private String gender;

    private String groupName;


  }
}
