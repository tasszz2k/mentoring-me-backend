package com.labate.mentoringme.service.cometchat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.labate.mentoringme.constant.ComeTChatConstant;
import com.labate.mentoringme.model.ComeTChatToken;
import com.labate.mentoringme.model.User;
import com.labate.mentoringme.repository.ComeTChatTokenRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComeTChatServiceImpl implements ComeTChatService {

  private static RestTemplate restTemplate = new RestTemplate();

  private final ComeTChatTokenRepository comeTChatTokenRepository;

  @Value("${cometchat.app-id}")
  private String appKey;

  @Value("${cometchat.region}")
  private String region;

  @Value("${cometchat.api-key}")
  private String apiKey;

  @Override
  public void addUserToDashboard(User user) {
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.add("apiKey", apiKey);

    Map<String, Object> map = new HashMap<>();
    map.put("uid", user.getId().toString());
    map.put("name", user.getFullName());
    map.put("avatar", user.getImageUrl());

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

    var url = String.format(ComeTChatConstant.CREATE_USER_COMETCHAT_URL, appKey, region);
    restTemplate.postForEntity(url, entity, Object.class);
  }

  @Override
  public void activeUser(Long userId) {
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.add("apiKey", apiKey);

    String[] uidsToActivate = new String[1];
    uidsToActivate[0] = userId.toString();
    Map<String, Object> map = new HashMap<>();
    map.put("uidsToActivate", uidsToActivate);

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

    var url = String.format(ComeTChatConstant.ACTIVE_USER_COMETCHAT_URL, appKey, region, userId);
    restTemplate.put(url, entity);
  }

  @Override
  public void inActiveUser(Long userId) {
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.add("apiKey", apiKey);

    String[] uidsToDeactivate = new String[1];
    uidsToDeactivate[0] = userId.toString();
    Map<String, Object> map = new HashMap<>();
    map.put("uidsToDeactivate", uidsToDeactivate);

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

    var url = String.format(ComeTChatConstant.INACTIVE_USER_COMETCHAT_URL, appKey, region, userId);
    restTemplate.exchange(url, HttpMethod.DELETE, entity, Object.class);
  }

  @Override
  public void uploadAvatarUser(Long userId, String imgUrl) {
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.add("apiKey", apiKey);

    Map<String, Object> map = new HashMap<>();
    map.put("avatar", imgUrl);

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

    var url = String.format(ComeTChatConstant.UPDATE_USER_COMETCHAT_URL, appKey, region);
    restTemplate.put(url, entity, userId);
  }

  @Override
  public String getToken(Long userId) {
    var comeTChat = comeTChatTokenRepository.findByUserId(userId);
    if (Objects.isNull(comeTChat)) {
      var headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
      headers.add("apiKey", apiKey);

      var url =
          String.format(ComeTChatConstant.CREATE_USER_TOKEN_COMETCHAT_URL, appKey, region, userId);
      HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);

      var response = restTemplate.postForEntity(url, entity, ObjectNode.class);
      var token = response.getBody().path("data").path("authToken").asText();
      addComeTChat(userId, token);
      return token;
    }
    return comeTChat.getToken();
  }

  private void addComeTChat(Long userId, String token) {
    var comeTChat = new ComeTChatToken();
    comeTChat.setUserId(userId);
    comeTChat.setToken(token);
    comeTChatTokenRepository.save(comeTChat);
  }

}
