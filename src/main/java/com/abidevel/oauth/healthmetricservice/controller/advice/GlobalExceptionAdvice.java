package com.abidevel.oauth.healthmetricservice.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.abidevel.oauth.healthmetricservice.exception.HealthProfileAlreadyExistsException;
import com.abidevel.oauth.healthmetricservice.exception.NonExistentHealthProfileException;

@RestControllerAdvice
public class GlobalExceptionAdvice {

  @ExceptionHandler(HealthProfileAlreadyExistsException.class)
  public ResponseEntity<String> handleHealthProfileAlreadyExists(
          HealthProfileAlreadyExistsException e) {
    return ResponseEntity.badRequest().body(e.getMessage());
  }

  @ExceptionHandler(NonExistentHealthProfileException.class)
  public ResponseEntity<String> handleNonExistentHealthProfile(
          NonExistentHealthProfileException e) {
    return ResponseEntity.notFound().build();
  }
}
