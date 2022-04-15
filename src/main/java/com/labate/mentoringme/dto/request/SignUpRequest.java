package com.labate.mentoringme.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import com.labate.mentoringme.constant.SocialProvider;
import com.labate.mentoringme.constant.UserRole;
import com.labate.mentoringme.validator.AcceptableRoles;
import com.labate.mentoringme.validator.ValidPassword;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpRequest {

  // private Long userID;

  private String providerUserId;

  private SocialProvider socialProvider;

  @NotBlank
  private String fullName;

  @NotBlank
  @Email
  private String email;

  @ValidPassword
  private String password;

  @AcceptableRoles(roles = {UserRole.ROLE_USER, UserRole.ROLE_MENTOR},
      message = "You only can be user or mentor")
  private UserRole role;

  public SignUpRequest(String providerUserId, String fullName, String email, String password,
      SocialProvider socialProvider) {
    this.providerUserId = providerUserId;
    this.fullName = fullName;
    this.email = email;
    this.password = password;
    this.socialProvider = socialProvider;
  }

  public static Builder getBuilder() {
    return new Builder();
  }

  public static class Builder {
    private String providerUserID;
    private String fullName;
    private String email;
    private String password;
    private SocialProvider socialProvider;

    public Builder addProviderUserID(final String userID) {
      this.providerUserID = userID;
      return this;
    }

    public Builder addFullName(final String fullName) {
      this.fullName = fullName;
      return this;
    }

    public Builder addEmail(final String email) {
      this.email = email;
      return this;
    }

    public Builder addPassword(final String password) {
      this.password = password;
      return this;
    }

    public Builder addSocialProvider(final SocialProvider socialProvider) {
      this.socialProvider = socialProvider;
      return this;
    }

    public SignUpRequest build() {
      return new SignUpRequest(providerUserID, fullName, email, password, socialProvider);
    }
  }
}
