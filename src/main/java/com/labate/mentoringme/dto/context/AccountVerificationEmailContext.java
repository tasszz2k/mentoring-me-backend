package com.labate.mentoringme.dto.context;

import com.labate.mentoringme.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.util.UriComponentsBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountVerificationEmailContext extends AbstractEmailContext {

  private String token;

  @Override
  public <T> void init(T context) {
    // we can do any common configuration setup here
    // like setting up some base URL and context
    User customer = (User) context; // we pass the customer information to the context
    put("firstName", customer.getFullName());
    setTemplateLocation("emails/email-verification");
    setSubject("Complete your registration");
    setFrom("no-reply@labate.com");
    setTo(customer.getEmail());
  }

  public void setToken(String token) {
    this.token = token;
    put("token", token);
  }

  public void buildVerificationUrl(final String baseURL, final String token) {
    final String url =
        UriComponentsBuilder.fromHttpUrl(baseURL)
            .path("/api/auth/verification/email")
            .queryParam("token", token)
            .toUriString();
    put("verificationURL", url);
  }
}
