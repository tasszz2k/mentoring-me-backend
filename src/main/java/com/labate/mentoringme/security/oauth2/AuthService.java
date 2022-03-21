package com.labate.mentoringme.security.oauth2;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.labate.mentoringme.exception.LoginFailException;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AuthService {
  private static final long MAXIMUM_CACHE_SIZE = 1000;

  @Value("${labate.security.failed-login.count}")
  private int maxFailedLogins;

  @Value("${labate.security.failed-login.time}")
  private int timeFailedLogins;

  @Autowired private AuthenticationManager authenticationManager;

  // Email - Login Fail Counter
  public final LoadingCache<String, Integer> loginFailCounterCache =
      CacheBuilder.newBuilder()
          .maximumSize(MAXIMUM_CACHE_SIZE)
          .expireAfterAccess(timeFailedLogins, TimeUnit.MINUTES)
          .build(
              new CacheLoader<>() {
                @Override
                public Integer load(@NotNull final String username) {
                  return 0;
                }
              });

  public Authentication getAuthentication(String email, String password) {
    Authentication authenticate;
    try {
      authenticate =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(email, password));
    } catch (Exception e) {
      increaseLoginFailCounter(email);
      throw new LoginFailException("Invalid username or password");
    }
    return authenticate;
  }

  private void increaseLoginFailCounter(String username) {
    var counter = loginFailCounterCache.getIfPresent(username);
    int value = counter + 1;
    loginFailCounterCache.put(username, value);
  }

  public boolean isBruteForceAttack(String username) {
    int counter = loginFailCounterCache.getUnchecked(username);
    return counter >= maxFailedLogins;
  }

}
