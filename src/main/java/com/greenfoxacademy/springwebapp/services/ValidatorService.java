package com.greenfoxacademy.springwebapp.services;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This interface contains general methods for validation purpose.
 *
 * @author MKJ
 */
public interface ValidatorService {
  /**
   * Collects names of missingFields to an Optional of unified error message.
   *
   * @param missingFields List of Strings representing the fields that are missing.
   * @return Generated Optional of error message based on the input list (missingFields). Empty optional if input List is empty.
   */
  Optional<String> getErrorMessageByMissingFields(List<String> missingFields);

  /**
   * Validates the value of getter based on the validator. Null check should be part of the implementation by default.
   * Returns a String Optional containing fieldName if the field is valid, empty Optional if it is invalid.
   *
   * @param fieldName Name of field that getter supplies the value of.
   * @param getter    Lambda impression that returns the value to validate. (getter method reference)
   * @param validator A Predicate defining the validation logic.
   * @param <T>       Type of field to validate.
   * @return Empty optional if valid and optional of fieldName if invalid.
   * @see Supplier
   * @see Predicate
   * @see javax.lang.model.element.TypeParameterElement
   * @see javax.lang.model.type.TypeVariable
   */
  <T> Optional<String> validateField(String fieldName, Supplier<T> getter, Predicate<T> validator);
}