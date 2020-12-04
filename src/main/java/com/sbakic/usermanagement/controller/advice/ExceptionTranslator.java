package com.sbakic.usermanagement.controller.advice;

import com.sbakic.usermanagement.config.ErrorConstants;
import com.sbakic.usermanagement.exception.EmailAlreadyTakenException;
import com.sbakic.usermanagement.exception.NotFoundException;
import com.sbakic.usermanagement.exception.UniqueConstraintViolatedException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling, SecurityAdviceTrait {

  private static final String TYPE_KEY = "errorType";
  private static final String FIELD_ERRORS_KEY = "fields";

  @Override
  public ResponseEntity<Problem> process(
      ResponseEntity<Problem> entity, @Nonnull NativeWebRequest request) {
    return new ResponseEntity<>(entity.getBody(), entity.getHeaders(), entity.getStatusCode());
  }

  @Override
  public ResponseEntity<Problem> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, @Nonnull final NativeWebRequest request
  ) {
    BindingResult result = ex.getBindingResult();
    List<FieldErrorVM> fieldErrors = result.getFieldErrors()
        .stream()
        .map(f -> new FieldErrorVM(f.getObjectName(), f.getField(), f.getDefaultMessage()))
        .collect(Collectors.toList());

    Problem problem = Problem.builder()
        .withTitle("Method argument not valid")
        .withStatus(defaultConstraintViolationStatus())
        .with(TYPE_KEY, ErrorConstants.VALIDATION_ERROR)
        .with(FIELD_ERRORS_KEY, fieldErrors)
        .build();

    return create(ex, problem, request);
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handleBadCredentialsException(
      BadCredentialsException ex, NativeWebRequest request
  ) {
    Problem problem = Problem.builder()
        .withStatus(Status.UNAUTHORIZED)
        .with(TYPE_KEY, ErrorConstants.BAD_CREDENTIALS)
        .build();

    return create(ex, problem, request);
  }

  @ExceptionHandler({UniqueConstraintViolatedException.class})
  public ResponseEntity<Problem> handleUniqueConstraintViolatedException(
      UniqueConstraintViolatedException ex, NativeWebRequest request
  ) {
    Problem problem = Problem.builder()
        .withStatus(Status.CONFLICT)
        .with(TYPE_KEY, ErrorConstants.CONSTRAINT_VALIDATION)
        .withDetail(ex.getMessage())
        .build();

    return create(ex, problem, request);
  }

  @ExceptionHandler({NotFoundException.class, EntityNotFoundException.class})
  public ResponseEntity<Problem> handleNotFoundException(
      RuntimeException ex, NativeWebRequest request
  ) {
    Problem problem = Problem.builder()
        .withStatus(Status.NOT_FOUND)
        .with(TYPE_KEY, ErrorConstants.NOT_FOUND)
        .withDetail(ex.getMessage())
        .build();

    return create(ex, problem, request);
  }

  @ExceptionHandler({EmailAlreadyTakenException.class})
  public ResponseEntity<Problem> handleEmailAlreadyTakenException(
      RuntimeException ex, NativeWebRequest request
  ) {
    Problem problem = Problem.builder()
        .withStatus(Status.CONFLICT)
        .with(TYPE_KEY, ErrorConstants.EMAIL_ALREADY_TAKEN)
        .withDetail(ex.getMessage())
        .build();

    return create(ex, problem, request);
  }
}
