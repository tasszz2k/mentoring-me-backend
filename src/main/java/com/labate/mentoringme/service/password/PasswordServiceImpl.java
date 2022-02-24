package com.labate.mentoringme.service.password;

import com.labate.mentoringme.exception.InvalidPasswordException;
import com.labate.mentoringme.exception.UserNotFoundException;
import com.labate.mentoringme.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PasswordServiceImpl implements PasswordService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public boolean changePassword(Long userId, String oldPassword, String newPassword) {
    var user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new InvalidPasswordException("Old password is incorrect");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    return true;
  }
}
