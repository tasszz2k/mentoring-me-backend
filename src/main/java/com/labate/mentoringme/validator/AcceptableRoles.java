package com.labate.mentoringme.validator;

import com.labate.mentoringme.constant.UserRole;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AcceptableRolesValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AcceptableRoles {

  String message() default "Invalid role";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  UserRole[] roles();
}
