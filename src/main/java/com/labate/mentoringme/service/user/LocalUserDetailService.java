package com.labate.mentoringme.service.user;

import com.labate.mentoringme.dto.mapper.RoleMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.exception.ResourceNotFoundException;
import com.labate.mentoringme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("localUserDetailService")
public class LocalUserDetailService implements UserDetailsService {

  @Autowired private UserService userService;

  @Override
  @Transactional
  public LocalUser loadUserByUsername(final String email) throws UsernameNotFoundException {
    User user = userService.findUserByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException("User " + email + " was not found in the database");
    }
    return createLocalUser(user);
  }

  @Transactional
  public LocalUser loadUserById(Long id) {
    User user =
        userService
            .findUserById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    return createLocalUser(user);
  }

  /**
   * @param user
   * @return
   */
  public LocalUser createLocalUser(User user) {
    return new LocalUser(
        user.getEmail(),
        user.getPassword(),
        user.isEnabled(),
        true,
        true,
        true,
        RoleMapper.buildSimpleGrantedAuthorities(user.getRoles()),
        user);
  }
}
