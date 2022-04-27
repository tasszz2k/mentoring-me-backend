package com.labate.mentoringme.service.cometchat;

import com.labate.mentoringme.dto.request.UpdateCometChatRequest;
import com.labate.mentoringme.model.User;

public interface ComeTChatService {
  void addUserToDashboard(User user);

  void activeUser(Long userId);

  void inActiveUser(Long userId);

  void updateUser(UpdateCometChatRequest updateRequest);

  String getToken(Long userId);
}
