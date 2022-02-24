package com.labate.mentoringme.service.verification;

import com.labate.mentoringme.dto.context.AccountVerificationEmailContext;
import com.labate.mentoringme.exception.InvalidTokenException;
import com.labate.mentoringme.model.SecureToken;
import com.labate.mentoringme.model.User;
import com.labate.mentoringme.repository.SecureTokenRepository;
import com.labate.mentoringme.repository.UserRepository;
import com.labate.mentoringme.security.token.SecureTokenService;
import com.labate.mentoringme.service.mail.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.mail.MessagingException;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AccountVerificationServiceImpl implements AccountVerificationService {

  private final SecureTokenService secureTokenService;
  private final SecureTokenRepository secureTokenRepository;
  private final EmailService emailService;
  private final UserRepository userRepository;

  @Value("${site.base.url.https}")
  private String baseURL;

  @Override
  public void sendRegistrationConfirmationEmail(User user) {
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
  }

  @Override
  public boolean verifyUser(String token) throws InvalidTokenException {
    SecureToken secureToken = secureTokenService.findByToken(token);
    if (Objects.isNull(secureToken)
        || !StringUtils.equals(token, secureToken.getToken())
        || secureToken.isExpired()) {
      throw new InvalidTokenException("Token is not valid");
    }
    var user = userRepository.findById(secureToken.getUser().getId()).orElse(null);
    if (Objects.isNull(user)) {
      return false;
    }
    user.setVerifiedEmail(true);
    userRepository.save(user); // let's same user details

    // we don't need invalid password now
    secureTokenService.removeToken(secureToken);
    return true;
  }
}
