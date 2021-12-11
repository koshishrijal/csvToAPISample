package com.kosh.csvrestapi.service;

import com.kosh.csvrestapi.batch.JobCompletionNotificationListener;
import com.kosh.csvrestapi.data.Employee;
import com.kosh.csvrestapi.data.EmployeeExcludingIncomeProjection;
import com.kosh.csvrestapi.data.Money;
import com.kosh.csvrestapi.exception.MoneyProcessorException;
import com.kosh.csvrestapi.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.employeeService = new EmployeeService(employeeRepository);
    }

    @Test
    void findByDepartment() throws MoneyProcessorException {
        Mockito.when(employeeRepository.findAllEmployee()).thenReturn(getEmployeeList_Initialized());
        List<EmployeeExcludingIncomeProjection> employeeList = this.employeeService.findByDepartmentWithoutSalary("ENG");
        Assertions.assertEquals(1,employeeList.size());
        Assertions.assertEquals(0,employeeList.stream().filter(e->!e.getDepartment().equals("ENG")).collect(Collectors.toList()).size());
    }

    @Test
    void findAboveAge() throws MoneyProcessorException {
        Mockito.when(employeeRepository.findAllEmployee()).thenReturn(getEmployeeList_Initialized());
        List<Employee> employeeList = this.employeeService.findAboveAge(29);
        Assertions.assertEquals(3,employeeList.size());
    }

    @Test
    void findAboveSalary_validIncome() throws MoneyProcessorException {
        Mockito.when(employeeRepository.findAllEmployee()).thenReturn(getEmployeeList_Initialized());
        List<Employee> employeeList = this.employeeService.findAboveSalary(new Money("$3000"));
        Assertions.assertEquals(2,employeeList.size());
    }

    @Test
    void findAboveSalary_InvalidSalaryWithoutCurrency() throws MoneyProcessorException {
        Mockito.when(employeeRepository.findAllEmployee()).thenReturn(getEmployeeList_Initialized());
       Assertions.assertThrows(MoneyProcessorException.class,()->this.employeeService.findAboveSalary(new Money("3000")));

    }

    @Test
    void findAboveSalary_InvalidSalaryInvalidNumberFormat() throws MoneyProcessorException {
        Mockito.when(employeeRepository.findAllEmployee()).thenReturn(getEmployeeList_Initialized());
        Assertions.assertThrows(MoneyProcessorException.class,()->this.employeeService.findAboveSalary(new Money("$5A5F")));
    }

    private static List<Employee> getEmployeeList_Initialized() throws MoneyProcessorException {
        List<Employee> employeeList = new ArrayList<>();
        Employee robert = Employee.builder().age(35).department("ENG")
                .firstName("Robert").lastName("Winner").income(new Money("$50000")).build();
        employeeList.add(robert);
        Employee dane = Employee.builder().age(29).department("OPER")
                .firstName("Dane").lastName("Dole").income(new Money("$4000")).build();
        employeeList.add(dane);
        Employee jackson = Employee.builder().age(30).department("OPER")
                .firstName("Dorothy").lastName("Jackson").income(new Money("$3000")).build();
        employeeList.add(jackson);
        Employee wayne = Employee.builder().age(32).department("FIELD")
                .firstName("Smith").lastName("Wayne").income(new Money("$3000")).build();
        employeeList.add(wayne);
        return employeeList;

    }
}