package com.labate.mentoringme.constant;

public enum SocialProvider {
  FACEBOOK("facebook"),
  GOOGLE("google"),
  LOCAL("local");

  private final String providerType;

  SocialProvider(final String providerType) {
    this.providerType = providerType;
  }

  public String getProviderType() {
    return providerType;
  }
}
