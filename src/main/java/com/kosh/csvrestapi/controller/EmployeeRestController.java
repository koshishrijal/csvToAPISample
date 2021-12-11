package com.kosh.csvrestapi.controller;

import com.kosh.csvrestapi.data.Money;
import com.kosh.csvrestapi.exception.MoneyProcessorException;
import com.kosh.csvrestapi.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeRestController {

    private final EmployeeService employeeService;

    public EmployeeRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping(value = "", params = {"department"})
    public ResponseEntity employeeByDepartmentWithoutSalary(@RequestParam("department") String department) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("employees", this.employeeService.findByDepartmentWithoutSalary(department));
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "", params = {"ageAbove"})
    public ResponseEntity employeeAboveAge(@RequestParam("ageAbove") int ageAbove) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("employees", this.employeeService.findAboveAge(ageAbove));
        return ResponseEntity.ok(data);
    }

    @GetMapping(value = "", params = {"salaryAbove"})
    public ResponseEntity employeeAboveSalary(@RequestParam("salaryAbove") String salaryAbove) throws MoneyProcessorException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("employees", this.employeeService.findAboveSalary(new Money(salaryAbove)));
        return ResponseEntity.ok(data);
    }
}
