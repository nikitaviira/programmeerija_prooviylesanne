package com.rik.programmeerija_prooviylesanne.config;

import com.rik.programmeerija_prooviylesanne.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorDto> handleRuntimeException(RuntimeException exception) {
    log.error(exception.getMessage());
    return status(INTERNAL_SERVER_ERROR).body(new ErrorDto(exception.getMessage()));
  }

  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<ErrorDto> handleServiceException(ServiceException exception) {
    return status(BAD_REQUEST).body(new ErrorDto(exception.getMessage()));
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException exception) {
    return status(NOT_FOUND).body(new ErrorDto(exception.getMessage()));
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    List<String> errorList = ex
        .getBindingResult()
        .getFieldErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .toList();
    return status(BAD_REQUEST).body(new ErrorDto("Validation failed: ", errorList));
  }
}