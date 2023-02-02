package com.example.deukgeun.trainer.request;

import com.example.deukgeun.trainer.entity.Profile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileRequest {
  private Long trainerUserId;

  private String path;

  @Builder
  public ProfileRequest(Long trainerUserId, String path) {
    this.trainerUserId = trainerUserId;
    this.path = path;
  }

  public static Profile create(ProfileRequest request) {
    return Profile.builder().trainerUserId(request.getTrainerUserId()).path(request.getPath())
        .build();
  }
}
