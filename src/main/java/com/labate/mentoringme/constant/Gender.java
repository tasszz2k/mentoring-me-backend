package com.labate.mentoringme.constant;

import java.util.stream.Stream;

public enum Gender {
  MALE(1),
  FEMALE(2),
  OTHER(3);

  private final Integer value;

  Gender(Integer value) {
    this.value = value;
  }

  public static Gender of(Integer value) {
    return Stream.of(Gender.values())
        .filter(type -> type.getValue().equals(value))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("Invalid gender with value: " + value));
  }

  public Integer getValue() {
    return value;
  }
}
