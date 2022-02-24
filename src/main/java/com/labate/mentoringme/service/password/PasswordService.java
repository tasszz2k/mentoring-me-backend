package com.labate.mentoringme.service.password;

public interface PasswordService {
  boolean changePassword(Long userId, String oldPassword, String newPassword);
}
