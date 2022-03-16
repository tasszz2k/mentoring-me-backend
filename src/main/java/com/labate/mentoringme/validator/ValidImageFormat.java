package com.labate.mentoringme.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE, FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ImageFormatValidator.class)
@Documented
public @interface ValidImageFormat {

  String message() default "Invalid Image Format";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
