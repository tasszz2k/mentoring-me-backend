package com.labate.mentoringme.validator;

import com.labate.mentoringme.constant.UserRole;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AcceptableRoleRegisterValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AcceptableRoleRegister {

  String message() default "Invalid role";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  UserRole[] roles();
}
