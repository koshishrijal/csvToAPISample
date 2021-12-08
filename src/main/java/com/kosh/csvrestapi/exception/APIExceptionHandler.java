package com.kosh.csvrestapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

}
