package com.labate.mentoringme.exception.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.labate.mentoringme.dto.response.ErrorResponse;
import com.labate.mentoringme.dto.response.FieldErrorResponse;
import com.labate.mentoringme.dto.response.InvalidInputResponse;
import com.labate.mentoringme.exception.*;
import com.labate.mentoringme.exception.http.ResponseError;
import com.labate.mentoringme.exception.http.ResponseException;
import com.labate.mentoringme.internationalization.LanguageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
;

@ControllerAdvice
@Slf4j
@Order
public class ExceptionHandleAdvice {

  private final LanguageService languageService;

  @Autowired
  public ExceptionHandleAdvice(LanguageService languageService) {
    this.languageService = languageService;
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse<Void>> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e, HttpServletRequest request) {
    log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage());
    Set<FieldErrorResponse> errors = new HashSet<>();
    errors.add(
        FieldErrorResponse.builder()
            .field(e.getParameter().getParameterName())
            .message(e.getMessage())
            .build());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new InvalidInputResponse(
                BadRequestError.INVALID_INPUT.getCode(),
                e.getMessage(),
                BadRequestError.INVALID_INPUT.getName(),
                errors));
  }

  @ExceptionHandler(MissingPathVariableException.class)
  public ResponseEntity<ErrorResponse<Void>> handleMissingPathVariableException(
      MissingPathVariableException e, HttpServletRequest request) {
    log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage());
    Set<FieldErrorResponse> errors = new HashSet<>();
    errors.add(
        FieldErrorResponse.builder()
            .field(e.getParameter().getParameterName())
            .message(e.getMessage())
            .build());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new InvalidInputResponse(
                BadRequestError.MISSING_PATH_VARIABLE.getCode(),
                e.getMessage(),
                BadRequestError.MISSING_PATH_VARIABLE.getName(),
                errors));
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse<Void>> handleRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
    log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage());
    Set<FieldErrorResponse> errors = new HashSet<>();
    errors.add(FieldErrorResponse.builder().field(e.getMethod()).message(e.getMessage()).build());
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
        .body(
            new InvalidInputResponse(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                e.getMessage(),
                BadRequestError.INVALID_INPUT.getName(),
                errors));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<InvalidInputResponse> handleValidationException(
      ConstraintViolationException e, HttpServletRequest request) {
    String queryParam;
    String object;
    String errorMessage;
    Set<FieldErrorResponse> errors = new HashSet<>();
    for (ConstraintViolation constraintViolation : e.getConstraintViolations()) {
      String queryParamPath = constraintViolation.getPropertyPath().toString();
      log.debug("queryParamPath = {}", queryParamPath);
      queryParam =
          queryParamPath.contains(".")
              ? queryParamPath.substring(queryParamPath.indexOf(".") + 1)
              : queryParamPath;
      object =
          queryParamPath.split("\\.").length > 1
              ? queryParamPath.substring(
                  queryParamPath.indexOf(".") + 1, queryParamPath.lastIndexOf("."))
              : queryParamPath;
      errorMessage =
          languageService.getMessage(
              constraintViolation.getMessage(), constraintViolation.getMessage());
      errors.add(
          FieldErrorResponse.builder()
              .field(queryParam)
              .objectName(object)
              .message(errorMessage)
              .build());
    }
    InvalidInputResponse invalidInputResponse;
    if (errors.size() >= 1) {
      long count = errors.size();
      invalidInputResponse =
          errors.stream()
              .skip(count - 1)
              .findFirst()
              .map(
                  fieldErrorResponse ->
                      new InvalidInputResponse(
                          BadRequestError.INVALID_INPUT.getCode(),
                          fieldErrorResponse.getMessage(),
                          fieldErrorResponse.getObjectName(),
                          errors))
              .orElse(null);
    } else {
      invalidInputResponse =
          new InvalidInputResponse(
              BadRequestError.INVALID_INPUT.getCode(),
              languageService.getMessage(
                  BadRequestError.INVALID_INPUT.getMessage(), "Invalid request arguments"),
              BadRequestError.INVALID_INPUT.getName(),
              errors);
    }
    log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(invalidInputResponse);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<InvalidInputResponse> handleValidationException(
      HttpMessageNotReadableException e, HttpServletRequest request) throws IOException {
    log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
    Throwable cause = e.getCause();
    InvalidInputResponse invalidInputResponse = null;
    if (cause instanceof InvalidFormatException) {
      InvalidFormatException invalidFormatException = (InvalidFormatException) cause;
      String fieldPath =
          invalidFormatException.getPath().stream()
              .map(JsonMappingException.Reference::getFieldName)
              .collect(Collectors.joining("."));
      invalidInputResponse =
          new InvalidInputResponse(
              BadRequestError.INVALID_INPUT.getCode(),
              e.getMessage(),
              BadRequestError.INVALID_INPUT.name(),
              Collections.singleton(
                  FieldErrorResponse.builder()
                      .field(fieldPath)
                      .message("Invalid input format")
                      .build()));
    }
    if (cause instanceof JsonParseException) {
      JsonParseException jsonParseException = (JsonParseException) cause;
      invalidInputResponse =
          new InvalidInputResponse(
              BadRequestError.INVALID_INPUT.getCode(),
              BadRequestError.INVALID_INPUT.getMessage(),
              BadRequestError.INVALID_INPUT.name(),
              Collections.singleton(
                  FieldErrorResponse.builder()
                      .field(jsonParseException.getProcessor().getCurrentName())
                      .message(jsonParseException.getMessage())
                      .build()));
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(invalidInputResponse);
  }

  @ExceptionHandler(ResponseException.class)
  public ResponseEntity<ErrorResponse<Void>> handleResponseException(
      ResponseException e, HttpServletRequest request) {
    log.warn(
        "Failed to handle request {}: {}", request.getRequestURI(), e.getError().getMessage(), e);
    ResponseError error = e.getError();
    String message =
        languageService.getMessage(error.getName(), e.getError().getMessage(), e.getParams());
    return ResponseEntity.status(error.getStatus())
        .body(
            ErrorResponse.<Void>builder()
                .code(error.getCode())
                .error(error.getName())
                .message(message)
                .build());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<InvalidInputResponse> handleValidationException(
      MethodArgumentNotValidException e, HttpServletRequest request) {
    BindingResult bindingResult = e.getBindingResult();
    Set<FieldErrorResponse> fieldErrors =
        bindingResult.getAllErrors().stream()
            .map(
                objectError -> {
                  try {
                    FieldError fieldError = (FieldError) objectError;
                    String message =
                        languageService.getMessage(
                            fieldError.getDefaultMessage(), fieldError.getDefaultMessage());
                    return FieldErrorResponse.builder()
                        .field(fieldError.getField())
                        .objectName(fieldError.getObjectName())
                        .message(message)
                        .build();
                  } catch (ClassCastException ex) {
                    return null;
                  }
                })
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new InvalidInputResponse(
                BadRequestError.INVALID_INPUT.getCode(),
                e.getMessage(),
                BadRequestError.INVALID_INPUT.getName(),
                fieldErrors));
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<ErrorResponse<Void>> handleResponseException(
      Exception e, HttpServletRequest request) {
    ResponseError error = InternalServerError.INTERNAL_SERVER_ERROR;
    log.error("Failed to handle request " + request.getRequestURI() + ": " + error.getMessage(), e);
    languageService.getMessage(
        InternalServerError.INTERNAL_SERVER_ERROR.getName(), "There are somethings wrong: {0}", e);
    return ResponseEntity.status(error.getStatus())
        .body(
            ErrorResponse.<Void>builder()
                .code(error.getCode())
                .error(error.getName())
                .message(e.getMessage())
                .build());
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<InvalidInputResponse> handleValidationException(
      MissingServletRequestParameterException e, HttpServletRequest request) {
    log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new InvalidInputResponse(
                BadRequestError.INVALID_INPUT.getCode(),
                e.getMessage(),
                BadRequestError.INVALID_INPUT.getName(),
                Collections.singleton(
                    FieldErrorResponse.builder()
                        .field(e.getParameterName())
                        .message(e.getMessage())
                        .build())));
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<InvalidInputResponse> handleValidationException(
      BindException e, HttpServletRequest request) {
    log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
    Set<FieldErrorResponse> fieldsErrors =
        e.getFieldErrors().stream()
            .map(
                fieldError ->
                    FieldErrorResponse.builder()
                        .field(fieldError.getField())
                        .objectName(fieldError.getObjectName())
                        .message(fieldError.getDefaultMessage())
                        .build())
            .collect(Collectors.toSet());
    String message =
        fieldsErrors.stream().map(FieldErrorResponse::getMessage).collect(Collectors.joining(";"));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new InvalidInputResponse(
                BadRequestError.INVALID_INPUT.getCode(),
                message,
                BadRequestError.INVALID_INPUT.name(),
                fieldsErrors));
  }

  @ExceptionHandler(MismatchedInputException.class)
  public ResponseEntity<InvalidInputResponse> handleValidationException(
      MismatchedInputException e, HttpServletRequest request) {
    log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new InvalidInputResponse(
                BadRequestError.INVALID_INPUT.getCode(),
                BadRequestError.INVALID_INPUT.getMessage(),
                BadRequestError.INVALID_INPUT.getName(),
                Collections.singleton(
                    FieldErrorResponse.builder().message(e.getMessage()).build())));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse<Void>> handleValidationException(
      AccessDeniedException e, HttpServletRequest request) {
    log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(
            ErrorResponse.<Void>builder()
                .error(AccessDeniedError.ACCESS_DENIED.getName())
                .message(
                    "You were not permitted to request "
                        + request.getMethod()
                        + " "
                        + request.getRequestURI())
                .build());
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse<Void>> handleUserNotFoundException(
      UserNotFoundException e, HttpServletRequest request) {
    ResponseError error = NotFoundError.USER_NOT_FOUND;
    log.error("Failed to handle request " + request.getRequestURI() + ": " + error.getMessage(), e);
    return ResponseEntity.status(error.getStatus())
        .body(
            ErrorResponse.<Void>builder()
                .code(error.getCode())
                .error(error.getName())
                .message(MessageFormat.format(error.getMessage(), e.getMessage()))
                .build());
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<ErrorResponse<Void>> handleCategoryNotFoundException(
      CategoryNotFoundException e, HttpServletRequest request) {
    ResponseError error = NotFoundError.CATEGORY_NOT_FOUND;
    log.error("Failed to handle request " + request.getRequestURI() + ": " + error.getMessage(), e);
    return ResponseEntity.status(error.getStatus())
        .body(
            ErrorResponse.<Void>builder()
                .code(error.getCode())
                .error(error.getName())
                .message(MessageFormat.format(error.getMessage(), e.getMessage()))
                .build());
  }

  @ExceptionHandler(MentorshipRequestNotFoundException.class)
  public ResponseEntity<ErrorResponse<Void>> handleMentorshipRequestNotFoundException(
      MentorshipRequestNotFoundException e, HttpServletRequest request) {
    ResponseError error = NotFoundError.MENTORSHIP_REQUEST_NOT_FOUND;
    log.error("Failed to handle request " + request.getRequestURI() + ": " + error.getMessage(), e);
    return ResponseEntity.status(error.getStatus())
        .body(
            ErrorResponse.<Void>builder()
                .code(error.getCode())
                .error(error.getName())
                .message(MessageFormat.format(error.getMessage(), e.getMessage()))
                .build());
  }

  @ExceptionHandler(QuizNotFoundException.class)
  public ResponseEntity<ErrorResponse<Void>> handleQuizNotFoundException(
      QuizNotFoundException e, HttpServletRequest request) {
    ResponseError error = NotFoundError.QUIZ_NOT_FOUND;
    log.error("Failed to handle request " + request.getRequestURI() + ": " + error.getMessage(), e);
    return ResponseEntity.status(error.getStatus())
        .body(
            ErrorResponse.<Void>builder()
                .code(error.getCode())
                .error(error.getName())
                .message(MessageFormat.format(error.getMessage(), e.getMessage()))
                .build());
  }

  @ExceptionHandler(UserAlreadyExistAuthenticationException.class)
  public ResponseEntity<ErrorResponse<Void>> handleUserAlreadyExistAuthenticationException(
      UserAlreadyExistAuthenticationException e, HttpServletRequest request) {
    ResponseError error = InvalidInputError.USER_ALREADY_EXISTED;
    log.error("Failed to handle request " + request.getRequestURI() + ": " + error.getMessage(), e);
    return ResponseEntity.status(error.getStatus())
        .body(
            new InvalidInputResponse(
                error.getCode(),
                MessageFormat.format(error.getMessage(), e.getMessage()),
                error.getName(),
                Collections.singleton(
                    FieldErrorResponse.builder().message(e.getMessage()).build())));
  }

  @ExceptionHandler(InvalidTokenException.class)
  public ResponseEntity<ErrorResponse<Void>> handleInvalidTokenException(
      InvalidTokenException e, HttpServletRequest request) {
    ResponseError error = UnauthorizedError.FORBIDDEN_ACCESS_TOKEN;
    log.error("Failed to handle request " + request.getRequestURI() + ": " + error.getMessage(), e);
    return ResponseEntity.status(error.getStatus())
        .body(
            new InvalidInputResponse(
                error.getCode(),
                error.getMessage(),
                error.getName(),
                Collections.singleton(
                    FieldErrorResponse.builder().message(e.getMessage()).build())));
  }
}
