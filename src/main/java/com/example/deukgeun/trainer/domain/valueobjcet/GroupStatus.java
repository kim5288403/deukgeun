package com.example.deukgeun.trainer.domain.valueobjcet;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GroupStatus {
  Y, N;

  @JsonCreator
  public static GroupStatus fromEnum(String val) {
    for (GroupStatus value : GroupStatus.values()) {
      if (value.name().equals(val)) {
        return value;
      }
    }
    return null;
  }
}
