package com.labate.mentoringme.validator;

import com.labate.mentoringme.dto.request.SignUpRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
    implements ConstraintValidator<PasswordMatches, SignUpRequest> {

  @Override
  public boolean isValid(final SignUpRequest user, final ConstraintValidatorContext context) {
    return user.getPassword().equals(user.getMatchingPassword());
  }
}
