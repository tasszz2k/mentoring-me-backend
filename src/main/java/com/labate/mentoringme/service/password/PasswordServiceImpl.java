package com.labate.mentoringme.service.password;

import com.labate.mentoringme.dto.context.ForgotPasswordEmailContext;
import com.labate.mentoringme.dto.request.ForgotPasswordRequest;
import com.labate.mentoringme.dto.request.ResetPasswordRequest;
import com.labate.mentoringme.exception.InvalidPasswordException;
import com.labate.mentoringme.exception.InvalidTokenException;
import com.labate.mentoringme.exception.UserNotFoundException;
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

@RequiredArgsConstructor
@Service
public class PasswordServiceImpl implements PasswordService {

  private final PasswordEncoder passwordEncoder;
  private final UserService userService;
  private final UserRepository userRepository;
  private final SecureTokenService secureTokenService;
  private final EmailService emailService;

  @Value("${site.base.url.https}")
  private String baseURL;

  @Override
  public boolean changePassword(Long userId, String oldPassword, String newPassword) {
    var user =
        userService
            .findUserById(userId)
            .orElseThrow(() -> new UserNotFoundException("id = " + userId));

    if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
      throw new InvalidPasswordException("Old password is incorrect");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    return true;
  }

  @Override
  public void forgottenPassword(ForgotPasswordRequest request) throws UserNotFoundException {
    String email = request.getEmail();
    String phoneNumber = request.getPhoneNumber();
    User user = null;
    if (email != null) {
      user = userService.findUserByEmail(email);
      if (user == null) {
        throw new UserNotFoundException("email = " + email);
      }
      sendResetPasswordByEmail(user);
    } else if (phoneNumber != null) {
      // TODO: implement phone number reset password
      // user = userService.findUserByPhoneNumber(phoneNumber);
    }
  }

  @Override
  public boolean resetPassword(ResetPasswordRequest request) throws InvalidTokenException {
    var token = request.getToken();
    var newPassword = request.getNewPassword();
    var email = request.getEmail();

    var secureToken = secureTokenService.getValidSecureToken(token, email);
    var user = secureToken.getUser();

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    // Remove token
    secureTokenService.removeToken(secureToken);
    return true;
  }

  private void sendResetPasswordByEmail(User user) {
    var secureToken = secureTokenService.createSecureToken(user);
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
