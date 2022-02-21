package com.labate.mentoringme.dto;

public enum SocialProvider {
  FACEBOOK("facebook"),
  GOOGLE("google"),
  LOCAL("local");

  private final String providerType;

  public String getProviderType() {
    return providerType;
  }

  SocialProvider(final String providerType) {
    this.providerType = providerType;
  }
}
