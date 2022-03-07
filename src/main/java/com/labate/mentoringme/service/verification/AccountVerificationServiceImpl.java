package com.labate.mentoringme.service.verification;

import com.labate.mentoringme.dto.context.AccountVerificationEmailContext;
import com.labate.mentoringme.dto.request.VerifyTokenRequest;
import com.labate.mentoringme.exception.InvalidTokenException;
import com.labate.mentoringme.model.SecureToken;
import com.labate.mentoringme.model.User;
import com.labate.mentoringme.repository.SecureTokenRepository;
import com.labate.mentoringme.security.token.SecureTokenService;
import com.labate.mentoringme.service.mail.EmailService;
import com.labate.mentoringme.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@RequiredArgsConstructor
@Service
public class AccountVerificationServiceImpl implements AccountVerificationService {

  private final SecureTokenService secureTokenService;
  private final SecureTokenRepository secureTokenRepository;
  private final EmailService emailService;
  private final UserService userService;

  @Value("${site.base.url.https}")
  private String baseURL;

  @Override
  public boolean sendRegistrationConfirmationEmail(User user) {
    if (user.isVerifiedEmail()) {
      return false;
    }

    SecureToken secureToken = secureTokenService.createSecureToken(user);
    AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
    emailContext.init(user);
    emailContext.setToken(secureToken.getToken());
    emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
    try {
      emailService.sendMail(emailContext);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    return true;
  }

  @Override
  public boolean verifyUser(String token) throws InvalidTokenException {
    SecureToken secureToken = secureTokenService.getValidSecureToken(token);
    Long userId = secureToken.getUser().getId();
    var user = userService.findUserById(userId).orElseThrow();
    user.setVerifiedEmail(true);
    userService.save(user);

    secureTokenService.removeToken(secureToken);
    return true;
  }

  @Override
  public boolean verifyToken(VerifyTokenRequest request) throws InvalidTokenException {
    String token = request.getToken();
    String email = request.getEmail();

    secureTokenService.getValidSecureToken(token, email);
    return true;
  }
}
