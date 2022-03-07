package com.labate.mentoringme.security.token;

import com.labate.mentoringme.exception.InvalidTokenException;
import com.labate.mentoringme.model.SecureToken;
import com.labate.mentoringme.model.User;

public interface SecureTokenService {

  SecureToken createSecureToken(User user);

  void saveSecureToken(final SecureToken token);

  SecureToken findByToken(final String token);

  void removeToken(final SecureToken token);

  void removeTokenByToken(final String token);

  SecureToken getValidSecureToken(String token) throws InvalidTokenException;

  SecureToken getValidSecureToken(String token, String email) throws InvalidTokenException;
}
