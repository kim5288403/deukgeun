package com.example.deukgeun.global.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
  M, F;

  @JsonCreator
  public static Gender fromEnum(String val) {
    for (Gender value : Gender.values()) {
      if (value.name().equals(val)) {
        return value;
      }
    }
    return null;
  }
}
