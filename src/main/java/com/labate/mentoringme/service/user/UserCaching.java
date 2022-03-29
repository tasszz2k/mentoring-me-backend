package com.labate.mentoringme.service.user;

import com.labate.mentoringme.model.User;
import com.labate.mentoringme.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserCaching {

  private final UserRepository userRepository;

  // @Value("${labate.cache.user.expiration-time}")
  // private int timeCacheUser;
  //
  // public final LoadingCache<Long, BasicUserInfo> basicUserInfoCache =
  //     CacheBuilder.newBuilder()
  //         .maximumSize(AppConstant.MAXIMUM_CACHE_SIZE)
  //         .expireAfterWrite(6, TimeUnit.HOURS)
  //         .build(
  //             new CacheLoader<>() {
  //               @Override
  //               public BasicUserInfo load(@NotNull final Long userId) {
  //                 var prj = userRepository.findBasicUserInfoById(userId);
  //                 return UserMapper.toBasicUserInfo(prj);
  //               }
  //             });

  /**
   * Only external method calls coming in through the proxy are intercepted. This means that
   * self-invocation, in effect, a method within the target object calling another method of the
   * target object, will not lead to an actual cache interception at runtime even if the invoked
   * method is marked with @Cacheable.
   *
   * @param id
   * @return
   */
  @Cacheable(value = "user", key = "#id")
  public Optional<User> findUserById(Long id) {
    return userRepository.findById(id);
  }
}
