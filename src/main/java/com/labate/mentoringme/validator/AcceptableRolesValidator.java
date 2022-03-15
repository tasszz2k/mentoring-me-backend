package com.labate.mentoringme.validator;

import com.labate.mentoringme.constant.UserRole;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AcceptableRolesValidator implements ConstraintValidator<AcceptableRoles, UserRole> {

  UserRole[] acceptableRoles;

  @Override
  public void initialize(AcceptableRoles constraintAnnotation) {
    acceptableRoles = constraintAnnotation.roles();
  }

  @Override
  public boolean isValid(UserRole role, ConstraintValidatorContext context) {
    for (UserRole acceptableRole : acceptableRoles) {
      if (acceptableRole.equals(role)) {
        return true;
      }
    }
    return false;
  }
}
