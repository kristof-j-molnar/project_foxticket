package com.greenfoxacademy.springwebapp.services;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface ValidatorService {
  Optional<String> getErrorMessageByMissingFields(List<String> missingFields);

  <T> Optional<String> validateField(String fieldName, Supplier<T> getter, Predicate<T> validator);
}