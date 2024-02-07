package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.ArticleRequestDTO;
import com.greenfoxacademy.springwebapp.services.ValidatorService;
import com.greenfoxacademy.springwebapp.services.ValidatorServiceImp;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidatorServiceImpTest {

  ValidatorService validatorService = new ValidatorServiceImp();

  @Test
  void getErrorMessageByMissingFields_WithListSizeOf5_ReturnsCorrectStringOptional() {
    List<String> missingFields = List.of("name", "title", "content", "description", "timestamp")
        .stream().collect(Collectors.toList());
    Optional<String> errorMessage = validatorService.getErrorMessageByMissingFields(missingFields);
    assertEquals(Optional.of("Name, title, content, description and timestamp are required."),
        errorMessage);
  }

  @Test
  void getErrorMessageByMissingFields_WithListSizeOf2_ReturnsCorrectStringOptional() {
    List<String> missingFields = List.of("name", "title")
        .stream().collect(Collectors.toList());
    Optional<String> errorMessage = validatorService.getErrorMessageByMissingFields(missingFields);
    assertEquals(Optional.of("Name and title are required."), errorMessage);
  }

  @Test
  void getErrorMessageByMissingFields_WithListSizeOf1_ReturnsCorrectStringOptional() {
    List<String> missingFields = new ArrayList<>();
    missingFields.add("name");
    Optional<String> errorMessage = validatorService.getErrorMessageByMissingFields(missingFields);
    assertEquals(Optional.of("Name is required."), errorMessage);
  }

  @Test
  void getErrorMessageByMissingFields_WithEmptyList_ReturnsEmptyOptional() {
    List<String> missingFields = new ArrayList<>();
    Optional<String> errorMessage = validatorService.getErrorMessageByMissingFields(missingFields);
    assertEquals(Optional.empty(), errorMessage);
  }

  @Test
  void validateField_withValidField_returnsEmptyOptional() {
    ArticleRequestDTO requestDTO = new ArticleRequestDTO("title", "content");
    Optional<String> fieldIfValid = validatorService.validateField("title", requestDTO::getTitle, Predicate.not(String::isBlank));
    assertEquals(Optional.empty(), fieldIfValid);
  }

  @Test
  void validateField_withBlankField_returnsEmptyOptional() {
    ArticleRequestDTO requestDTO = new ArticleRequestDTO("    ", "content");
    Optional<String> fieldIfValid = validatorService.validateField("title", requestDTO::getTitle, Predicate.not(String::isBlank));
    assertEquals(Optional.of("title"), fieldIfValid);
  }

  @Test
  void validateField_withNullField_returnsEmptyOptional() {
    ArticleRequestDTO requestDTO = new ArticleRequestDTO();
    Optional<String> fieldIfValid = validatorService.validateField("title", requestDTO::getTitle, Predicate.not(String::isBlank));
    assertEquals(Optional.of("title"), fieldIfValid);
  }

  @Test
  void validateField_withIncorrectEmailInputCheckingAtCharacter_returnsOptionalOfFieldName() {
    Optional<String> fieldIfValid = validatorService.validateField("e-mail", () -> "something.com", s -> s.contains("@"));
    assertEquals(Optional.of("e-mail"), fieldIfValid);
  }

  @Test
  void validateField_withCorrectEmailInputCheckingAtCharacter_returnsEmptyOptional() {
    Optional<String> fieldIfValid = validatorService.validateField("e-mail", () -> "anything@something.com", s -> s.contains("@"));
    assertEquals(Optional.empty(), fieldIfValid);
  }

  @Test
  void validateField_withPasswordOf5CharactersCheckingLengthBiggerThan8_returnsOptionalOfFieldName() {
    Optional<String> fieldIfValid = validatorService.validateField("password", () -> "short", s -> s.length() > 8);
    assertEquals(Optional.of("password"), fieldIfValid);
  }

  @Test
  void validateField_withPasswordOf9CharactersCheckingLengthBiggerThan8_returnsEmptyOptional() {
    Optional<String> fieldIfValid = validatorService.validateField("password", () -> "something", s -> s.length() > 8);
    assertEquals(Optional.empty(), fieldIfValid);
  }
}