package com.labate.mentoringme.service.cometchat;

import com.labate.mentoringme.model.User;

public interface ComeTChatService {
  void addUserToDashboard(User user);

  void activeUser(Long userId);

  void inActiveUser(Long userId);

  void uploadAvatarUser(Long userId, String imgUrl);

  String getToken(Long userId);
}
