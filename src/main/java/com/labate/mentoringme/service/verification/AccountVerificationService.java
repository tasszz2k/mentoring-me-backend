package com.labate.mentoringme.service.verification;

import com.labate.mentoringme.exception.InvalidTokenException;
import com.labate.mentoringme.model.User;

public interface AccountVerificationService {
  void sendRegistrationConfirmationEmail(final User user);
  boolean verifyUser(final String token) throws InvalidTokenException;

}
