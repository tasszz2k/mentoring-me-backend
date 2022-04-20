package com.labate.mentoringme.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.labate.mentoringme.model.User;

@Component
public class ComeTChatUtils {

  private static final String CREATE_USER_COMETCHAT_URL = "https://%s.api-%s.cometchat.io/v3/users";

  private static final String UPDATE_USER_COMETCHAT_URL =
      "https://%s.api-%s.cometchat.io/v3/users/{uid}";

  private static final String ACTIVE_USER_COMETCHAT_URL = "https://%s.api-%s.cometchat.io/v3/users";

  private static final String INACTIVE_USER_COMETCHAT_URL =
      "https://%s.api-%s.cometchat.io/v3/users";

  private static final String CREATE_USER_TOKEN_COMETCHAT_URL =
      "https://%s.api-%s.cometchat.io/v3/users/%s/auth_tokens";

  private static RestTemplate restTemplate = new RestTemplate();

  @Value("${cometchat.app-id}")
  private String appKey;

  @Value("${cometchat.region}")
  private String region;

  @Value("${cometchat.api-key}")
  private String apiKey;

  public void addUserToDashboard(User user) {
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.add("apiKey", apiKey);

    Map<String, Object> map = new HashMap<>();
    map.put("uid", user.getId().toString());
    map.put("name", user.getFullName());

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

    var url = String.format(CREATE_USER_COMETCHAT_URL, appKey, region);
    restTemplate.postForEntity(url, entity, Object.class);
  }

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

    var url = String.format(ACTIVE_USER_COMETCHAT_URL, appKey, region, userId);
    restTemplate.put(url, entity);
  }

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

    var url = String.format(ACTIVE_USER_COMETCHAT_URL, appKey, region, userId);
    restTemplate.exchange(url, HttpMethod.DELETE, entity, Object.class);
  }

  public void uploadAvatarUser(Long userId, String imgUrl) {
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.add("apiKey", apiKey);

    Map<String, Object> map = new HashMap<>();
    map.put("avatar", imgUrl);

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

    var url = String.format(UPDATE_USER_COMETCHAT_URL, appKey, region);
    restTemplate.put(url, entity, userId);
  }

  public String getToken(Long userId) {
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.add("apiKey", apiKey);

    var url = String.format(CREATE_USER_TOKEN_COMETCHAT_URL, appKey, region, userId);
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);

    var response = restTemplate.postForEntity(url, entity, ObjectNode.class);
    return response.getBody().path("data").path("authToken").asText();
  }

}
