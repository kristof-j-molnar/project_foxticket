package com.greenfoxacademy.springwebapp.unit;

import com.greenfoxacademy.springwebapp.dtos.ArticleRequestDTO;
import com.greenfoxacademy.springwebapp.services.ValidatorService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidatorServiceTest {

  ValidatorService validatorService = new ValidatorService();

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
    Optional<String> fieldIfValid = validatorService.validateField("title", requestDTO::getTitle, String::isBlank);
    assertEquals(Optional.empty(), fieldIfValid);
  }

  @Test
  void validateField_withBlankField_returnsEmptyOptional() {
    ArticleRequestDTO requestDTO = new ArticleRequestDTO("    ", "content");
    Optional<String> fieldIfValid = validatorService.validateField("title", requestDTO::getTitle, String::isBlank);
    assertEquals(Optional.of("title"), fieldIfValid);
  }

  @Test
  void validateField_withNullField_returnsEmptyOptional() {
    ArticleRequestDTO requestDTO = new ArticleRequestDTO();
    Optional<String> fieldIfValid = validatorService.validateField("title", requestDTO::getTitle, String::isBlank);
    assertEquals(Optional.of("title"), fieldIfValid);
  }

  @Test
  void validateArticleRequestDTO_WithEmptyFields_ReturnEmptyOptional() {
    ArticleRequestDTO requestDTO = new ArticleRequestDTO();
    assertEquals(Optional.of("Title and content are required."), validatorService.validateArticleRequestDTO(requestDTO));
  }

  @Test
  void validateArticleRequestDTO_withEmptyContent_ReturnsCorrectStringOptional() {
    ArticleRequestDTO requestDTO = new ArticleRequestDTO("title", "");
    assertEquals(Optional.of("Content is required."), validatorService.validateArticleRequestDTO(requestDTO));
  }

  @Test
  void validateArticleRequestDTO_withValidDTO_ReturnsCorrectStringOptional() {
    ArticleRequestDTO requestDTO = new ArticleRequestDTO("title", "content");
    assertEquals(Optional.empty(), validatorService.validateArticleRequestDTO(requestDTO));
  }
}