package com.labate.mentoringme.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
  private final AuthenticationManager authenticationManager;

  public Authentication getAuthentication(String email, String password) {
    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, password));
  }
}
