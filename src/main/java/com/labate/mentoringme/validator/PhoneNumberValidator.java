package com.labate.mentoringme.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
  private final Pattern phonePattern = Pattern.compile("(^$|0[0-9]{9,10})");

  @Override
  public void initialize(ValidPhoneNumber constraintAnnotation) {}

  @Override
  public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
    return phonePattern.matcher(phoneNumber).matches();
  }
}
