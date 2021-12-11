package com.kosh.csvrestapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class APIExceptionHandler {
    @ExceptionHandler(MoneyProcessorException.class)
    public ResponseEntity<Map> handleMoneyProcessorException(MoneyProcessorException moneyProcessorException) {
        Map<String, Object> respMap = new LinkedHashMap<>();
        respMap.put("error", moneyProcessorException.getMessage());
        return ResponseEntity.status(moneyProcessorException.getStatusCode()).body(respMap);
    }

    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    public ResponseEntity<Map> handleParameterNotMatchedError(UnsatisfiedServletRequestParameterException exception) {
        Map<String, Object> respMap = new LinkedHashMap<>();
        respMap.put("error", exception.getMessage());
        return ResponseEntity.status(400).body(respMap);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        String error =
                ex.getName() + " should be of type " + ex.getRequiredType().getName();
        Map<String, Object> respMap = new LinkedHashMap<>();
        respMap.put("error", error);
        return ResponseEntity.status(400).body(respMap);
    }

}
