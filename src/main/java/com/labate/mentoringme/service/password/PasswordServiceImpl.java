package com.labate.mentoringme.service.password;

import com.labate.mentoringme.dto.context.ForgotPasswordEmailContext;
import com.labate.mentoringme.dto.request.ResetPasswordRequest;
import com.labate.mentoringme.exception.InvalidPasswordException;
import com.labate.mentoringme.exception.InvalidTokenException;
import com.labate.mentoringme.exception.UserNotFoundException;
import com.labate.mentoringme.model.SecureToken;
import com.labate.mentoringme.model.User;
import com.labate.mentoringme.repository.UserRepository;
import com.labate.mentoringme.security.token.SecureTokenService;
import com.labate.mentoringme.service.mail.EmailService;
import com.labate.mentoringme.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PasswordServiceImpl implements PasswordService {

  @Value("${site.base.url.https}")
  private String baseURL;

  private final PasswordEncoder passwordEncoder;
  private final UserService userService;
  private final UserRepository userRepository;
  private final SecureTokenService secureTokenService;
  private final EmailService emailService;

  @Override
  public boolean changePassword(Long userId, String oldPassword, String newPassword) {
    var user =
        userService
            .findUserById(userId)
            .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new InvalidPasswordException("Old password is incorrect");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    return true;
  }

  @Override
  public void forgottenPassword(ResetPasswordRequest userName) throws UserNotFoundException {
    String email = userName.getEmail();
    String phoneNumber = userName.getPhoneNumber();
    User user = null;
    if (email != null) {
      user = userService.findUserByEmail(email);
      if (user == null) {
        throw new UserNotFoundException("user not found!");
      }
      sendResetPasswordByEmail(user);
    } else if (phoneNumber != null) {
      // TODO: implement phone number reset password
      // user = userService.findUserByPhoneNumber(phoneNumber);
    }
  }

  @Override
  public boolean resetPassword(String token, String newPassword) throws InvalidTokenException {
    SecureToken secureToken = secureTokenService.getValidSecureToken(token);
    var user = userRepository.findById(secureToken.getUser().getId()).orElse(null);
    if (Objects.isNull(user)) {
      throw new InvalidTokenException("invalid token or user not found");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);

    return true;
  }

  private void sendResetPasswordByEmail(User user) {
    SecureToken secureToken = secureTokenService.createSecureToken(user);
    ForgotPasswordEmailContext emailContext = new ForgotPasswordEmailContext();
    emailContext.init(user);
    emailContext.setToken(secureToken.getToken());
    emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
    try {
      emailService.sendMail(emailContext);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}
