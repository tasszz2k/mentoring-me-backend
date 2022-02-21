package com.labate.mentoringme.security.oauth2;

import com.labate.mentoringme.dto.SocialProvider;
import com.labate.mentoringme.exception.OAuth2AuthenticationProcessingException;
import com.labate.mentoringme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  @Autowired private UserService userService;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest)
      throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
    try {
      Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
      String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();
      return userService.processUserRegistration(provider, attributes, null, null);
    } catch (AuthenticationException ex) {
      throw ex;
    } catch (Exception ex) {
      ex.printStackTrace();
      // Throwing an instance of AuthenticationException will trigger the
      // OAuth2AuthenticationFailureHandler
      throw new OAuth2AuthenticationProcessingException(ex.getMessage(), ex.getCause());
    }
  }

}
