package com.labate.mentoringme.dto.request;

import com.labate.mentoringme.constant.SocialProvider;
import com.labate.mentoringme.validator.PasswordMatches;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignUpRequest {

  private Long userID;

  private String providerUserId;

  private SocialProvider socialProvider;

  @NotEmpty private String fullName;

  @NotEmpty private String email;


  @Size(min = 6, message = "{Size.userDto.password}")
  private String password;

  @NotEmpty private String matchingPassword;

  public SignUpRequest(
      String providerUserId,
      String fullName,
      String email,
      String password,
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
