package com.greenfoxacademy.springwebapp.log;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
public class LoggingInterceptor implements HandlerInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    Map<String, String[]> parameterMap = request.getParameterMap();
    StringBuilder paramsStringBuilder = new StringBuilder();
    for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
      String paramName = entry.getKey();
      String[] paramValues = entry.getValue();
      paramsStringBuilder.append(paramName).append(": ");
      for (String paramValue : paramValues) {
        paramsStringBuilder.append(paramValue).append(", ");
      }
      paramsStringBuilder.delete(paramsStringBuilder.length() - 2, paramsStringBuilder.length());
      paramsStringBuilder.append("; ");
    }
    System.out.println("Logging informations: ------------------------------------------------------------------------ ");
    logger.info("Request method: {}", request.getMethod());
    logger.info("Path of the endpoint: {}", request.getRequestURI());
    logger.info("Request parameters: {}", paramsStringBuilder);
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    int statusCode = response.getStatus();
    if (statusCode >= 400) {
      logger.error("Response status code: {}", statusCode);
    } else {
      logger.info("Response status code: {}", statusCode);
    }
  }
}

