package com.greenfoxacademy.springwebapp.services;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LogicService {

  private List<String> getListOfMissingFields(Object inputObject) throws IllegalAccessException {
    Class<?> classOfObject = inputObject.getClass();
    Field[] fields = classOfObject.getDeclaredFields();
    List<String> missingFields = new ArrayList<>();
    for (Field field : fields) {
      field.setAccessible(true);
      Object value = field.get(inputObject);
      if (value == null || (value instanceof String && ((String) value).isBlank())) {
        missingFields.add(field.getName());
      }
    }
    return missingFields;
  }

  public Optional<String> getErrorMessageByMissingFields(Object inputObject) throws IllegalAccessException {
    List<String> missingFields = getListOfMissingFields(inputObject);
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
}