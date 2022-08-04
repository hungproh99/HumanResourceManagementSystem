package com.csproject.hrm.exception.errors;

import com.csproject.hrm.exception.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {
  @ExceptionHandler(CustomDataNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleCustomDataNotFoundExceptions(Exception e) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    e.printStackTrace(printWriter);
    String stackTrace = stringWriter.toString();
    return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace), status);
  }

  @ExceptionHandler(CustomParameterConstraintException.class)
  public ResponseEntity<ErrorResponse> handleCustomParameterConstraintExceptions(Exception e) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    return new ResponseEntity<>(new ErrorResponse(status, e.getMessage()), status);
  }

  @ExceptionHandler(CustomErrorException.class)
  public ResponseEntity<ErrorResponse> handleCustomErrorExceptions(Exception e) {
    CustomErrorException customErrorException = (CustomErrorException) e;
    HttpStatus status = customErrorException.getStatus();
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    customErrorException.printStackTrace(printWriter);
    String stackTrace = String.format(stringWriter.toString());
    return new ResponseEntity<>(
        new ErrorResponse(
            status, customErrorException.getMessage(), stackTrace, customErrorException.getData()),
        status);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity handleExceptions(Exception e) {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    e.printStackTrace(printWriter);
    String stackTrace = stringWriter.toString();
    return new ResponseEntity<>(new ErrorResponse(status, e.getMessage(), stackTrace), status);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    List<String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());

    return ResponseEntity.ok(new ErrorResponse(HttpStatus.BAD_REQUEST, errors.toString()));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> constraintViolationException(
      ConstraintViolationException ex, WebRequest request) {
    List<String> errors = new ArrayList<>();
    ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));

    return ResponseEntity.ok(new ErrorResponse(HttpStatus.BAD_REQUEST, errors.toString()));
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return ResponseEntity.ok(
        new ErrorResponse(
            HttpStatus.BAD_REQUEST, "Missing " + ex.getParameterName() + " in request param"));
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return ResponseEntity.ok(
        new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Invalid format for field "
                + StringUtils.substringsBetween(ex.getCause().getMessage(), "[", "]")[1].replaceAll(
                    "\\\"", "")));
  }
}