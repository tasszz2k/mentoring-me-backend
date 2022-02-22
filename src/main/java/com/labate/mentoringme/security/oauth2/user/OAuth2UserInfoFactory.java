package com.labate.mentoringme.security.oauth2.user;

import com.labate.mentoringme.constant.SocialProvider;
import com.labate.mentoringme.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {
  public static OAuth2UserInfo getOAuth2UserInfo(
      String registrationId, Map<String, Object> attributes) {
    if (registrationId.equalsIgnoreCase(SocialProvider.GOOGLE.getProviderType())) {
      return new GoogleOAuth2UserInfo(attributes);
    } else if (registrationId.equalsIgnoreCase(SocialProvider.FACEBOOK.getProviderType())) {
      return new FacebookOAuth2UserInfo(attributes);
    }else {
      throw new OAuth2AuthenticationProcessingException(
          "Sorry! Login with " + registrationId + " is not supported yet.");
    }
  }
}
