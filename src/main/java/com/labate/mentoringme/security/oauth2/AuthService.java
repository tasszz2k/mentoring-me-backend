package com.labate.mentoringme.security.oauth2;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.labate.mentoringme.constant.AppConstant;
import com.labate.mentoringme.exception.LoginFailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

  @Value("${labate.security.max-login-attempts}")
  private int maxLoginAttempts;

  @Value("${labate.security.failed-login.time}")
  private int timeFailedLogins;

  @Autowired private AuthenticationManager authenticationManager;

  private final LoadingCache<String, Integer> loginFailCounterCache =
      CacheBuilder.newBuilder()
          .maximumSize(AppConstant.MAXIMUM_CACHE_SIZE)
          .expireAfterWrite(5, TimeUnit.MINUTES)
          .build(
              new CacheLoader<>() {
                @Override
                public Integer load(String key) {
                  return 0;
                }
              });

  public Authentication getAuthentication(String email, String password) {
    try {
      var authentication = new UsernamePasswordAuthenticationToken(email, password);
      var authenticate = authenticationManager.authenticate(authentication);
      loginFailCounterCache.put(email, 0);
      return authenticate;
    } catch (DisabledException e) {
      throw new DisabledException(email);
    } catch (Exception e) {
      int times = increaseLoginFailCounter(email);
      throw new LoginFailException(String.valueOf(times));
    }
  }

  private int increaseLoginFailCounter(String username) {
    var counter = loginFailCounterCache.getUnchecked(username);
    counter++;
    loginFailCounterCache.put(username, counter);
    return counter;
  }

  public boolean isBruteForceAttack(String username) {
    int counter = loginFailCounterCache.getUnchecked(username);
    return counter >= maxLoginAttempts;
  }
}
