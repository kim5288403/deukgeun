package com.example.deukgeun.trainer.request;

import com.example.deukgeun.trainer.entity.Profile;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileRequest {
  private Long id;

  private String path;

  @Builder
  public ProfileRequest(String path) {
    this.path = path;
  }

  public static Profile create(ProfileRequest request) {
    return Profile.builder().path(request.getPath())
        .build();
  }
}
