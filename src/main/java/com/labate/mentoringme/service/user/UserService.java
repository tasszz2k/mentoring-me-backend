package com.labate.mentoringme.service.user;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.web.multipart.MultipartFile;
import com.labate.mentoringme.constant.MentorStatus;
import com.labate.mentoringme.dto.model.BasicUserInfo;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.model.UserDetails;
import com.labate.mentoringme.dto.request.FindUsersRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.SignUpRequest;
import com.labate.mentoringme.exception.UserAlreadyExistAuthenticationException;
import com.labate.mentoringme.exception.UserNotFoundException;
import com.labate.mentoringme.model.User;

public interface UserService {

  User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException;

  boolean existsByEmail(String email);

  User findUserByEmail(String email);

  Optional<User> findUserById(Long id);

  LocalUser findLocalUserById(Long id);

  LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes,
      OidcIdToken idToken, OidcUserInfo userInfo);

  User save(User user);

  void updateUserEnableStatus(Long userId, boolean enable) throws UserNotFoundException;

  Page<User> findAllUsers(PageCriteria pageCriteria, FindUsersRequest request);

  List<User> findAllByIds(Collection<Long> ids);

  String uploadAvatar(LocalUser localUser, MultipartFile image) throws IOException;

  void updateMentorStatus(Long userId, MentorStatus status);

  BasicUserInfo findBasicUserInfoByUserId(Long userId);

  Map<Long, BasicUserInfo> findBasicUserInfos(List<Long> userIds);

  List<UserDetails> findAllUserProfile(PageCriteria pageCriteria, FindUsersRequest request);

  UserDetails findUserProfileById(Long id);
}
