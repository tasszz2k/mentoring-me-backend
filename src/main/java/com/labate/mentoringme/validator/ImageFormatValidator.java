package com.labate.mentoringme.validator;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageFormatValidator implements ConstraintValidator<ValidImageFormat, MultipartFile> {

  @Override
  public void initialize(ValidImageFormat constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
    var extension = FilenameUtils.getExtension(file.getOriginalFilename());
    matcher = IMAGE_EXTENSION_PATTEN.matcher(Objects.requireNonNull(extension));
    return false;
  }

  private static final Pattern IMAGE_PATTEN =
      Pattern.compile("([^\\s]+(\\.(?i)(/bmp|jpg|gif|png|svg))$)");
  private static final Pattern IMAGE_EXTENSION_PATTEN = Pattern.compile("bmp|jpg|gif|png|svg");
  private static Matcher matcher;

  public static boolean validate(final String image) {
    matcher = IMAGE_PATTEN.matcher(image);
    return matcher.matches();
  }

  public static boolean validateExtension(final String extension) {
    matcher = IMAGE_EXTENSION_PATTEN.matcher(extension);
    return matcher.matches();
  }
}
