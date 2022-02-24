package com.labate.mentoringme.service.password;

import com.labate.mentoringme.dto.request.ResetPasswordRequest;
import com.labate.mentoringme.exception.InvalidTokenException;
import com.labate.mentoringme.exception.UserNotFoundException;

public interface PasswordService {
  boolean changePassword(Long userId, String oldPassword, String newPassword);

  void forgottenPassword(final ResetPasswordRequest userName) throws UserNotFoundException;

  boolean resetPassword(String token, String newPassword) throws InvalidTokenException;
}
