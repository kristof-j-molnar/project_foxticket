package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ArticleRequestDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Service
public class ValidatorService {

  public Optional<String> getErrorMessageByMissingFields(List<String> missingFields) {
    final int size = missingFields.size();
    if (size > 0) {
      missingFields.set(0, missingFields.get(0).substring(0, 1).toUpperCase() + missingFields.get(0).substring(1));
    }
    return switch (size) {
      case 0 -> Optional.empty();
      case 1 -> Optional.of(String.format("%s is required.", missingFields.get(0)));
      default -> Optional.of(String.format("%s and %s are required.",
          String.join(", ", missingFields.subList(0, size - 1)),
          missingFields.get(size - 1)));
    };
  }

  public <T> Optional<String> validateField(String fieldName, Supplier<T> getter, Predicate<T> validator) {
    T value = getter.get();
    if (value == null || validator.test(value)) {
      return Optional.of(fieldName);
    } else {
      return Optional.empty();
    }
  }

  public Optional<String> validateArticleRequestDTO(ArticleRequestDTO requestDTO) {
    List<String> missingFields = new ArrayList<>();
    validateField("title", requestDTO::getTitle, String::isBlank).ifPresent(missingFields::add);
    validateField("content", requestDTO::getContent, String::isBlank).ifPresent(missingFields::add);
    return getErrorMessageByMissingFields(missingFields);
  }
}