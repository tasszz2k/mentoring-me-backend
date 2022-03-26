package com.labate.mentoringme.service.user;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.labate.mentoringme.constant.AppConstant;
import com.labate.mentoringme.dto.mapper.UserMapper;
import com.labate.mentoringme.dto.model.BasicUserInfo;
import com.labate.mentoringme.repository.UserRepository;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class UserCaching {

  private final UserRepository userRepository;

  @Value("${labate.cache.user.expiration-time}")
  private int timeCacheUser;

  public final LoadingCache<Long, BasicUserInfo> basicUserInfoCache =
      CacheBuilder.newBuilder()
          .maximumSize(AppConstant.MAXIMUM_CACHE_SIZE)
          .expireAfterWrite(6, TimeUnit.HOURS)
          .build(
              new CacheLoader<>() {
                @Override
                public BasicUserInfo load(@NotNull final Long userId) {
                  var prj = userRepository.findBasicUserInfoById(userId);
                  return UserMapper.toBasicUserInfo(prj);
                }
              });
}
