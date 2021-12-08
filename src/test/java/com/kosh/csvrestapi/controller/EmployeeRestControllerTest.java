package com.kosh.csvrestapi.controller;

import com.kosh.csvrestapi.batch.JobCompletionNotificationListener;
import com.kosh.csvrestapi.data.Employee;
import com.kosh.csvrestapi.data.Money;
import com.kosh.csvrestapi.exception.MoneyProcessorException;
import com.kosh.csvrestapi.repository.EmployeeRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeRestControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private EmployeeRepository employeeRepository;


    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void employeeByDepartment_SingleResult() throws MoneyProcessorException {
        Mockito.when(employeeRepository.findAllEmployee()).thenReturn(getEmployeeList_Initialized());
        ResponseEntity responseEntity = this.restTemplate.exchange("http://localhost:" + port + "/employee?department=ENG", HttpMethod.GET,
                new HttpEntity<>(new HashMap()),
                Map.class);
        Assert.assertEquals(200, responseEntity.getStatusCodeValue());
        Map<String, Object> data;
        data = (Map<String, Object>) responseEntity.getBody();
        System.out.println(data);
        Assert.assertTrue(data.containsKey("employees"));
        List<Employee> employeeResultList = getEmployeeListFromMapResponse(data);
        Assert.assertEquals(1, employeeResultList.size());
        Assert.assertEquals(0, employeeResultList.stream().filter(e -> !e.getDepartment().equals("ENG")).collect(Collectors.toList()).size());
        Assert.assertEquals("Robert", employeeResultList.get(0).getFirstName());

    }

    @Test
    void employeeByDepartment_EmptyResult() throws MoneyProcessorException {
        Mockito.when(employeeRepository.findAllEmployee()).thenReturn(getEmployeeList_Initialized());
        ResponseEntity responseEntity = this.restTemplate.exchange("http://localhost:" + port + "/employee?department=TEST", HttpMethod.GET,
                new HttpEntity<>(new HashMap()),
                Map.class);
        Assert.assertEquals(200, responseEntity.getStatusCodeValue());
        Map<String, Object> data;
        data = (Map<String, Object>) responseEntity.getBody();
        System.out.println(data);
        Assert.assertTrue(data.containsKey("employees"));
        List<Employee> employeeResultList = getEmployeeListFromMapResponse(data);
        Assert.assertEquals(0, employeeResultList.size());
    }

    private List<Employee> getEmployeeListFromMapResponse(Map data) throws MoneyProcessorException {
        List<Employee> employeeList = new ArrayList<>();
        if (!data.containsKey("employees") || data.get("employees") == null) {
            return employeeList;
        }
        List<Map> employeeListRaw = (List) data.get("employees");
        if (employeeListRaw.isEmpty()) {
            return employeeList;
        }
        for (Map empRaw : employeeListRaw) {
            Employee employee = new Employee();
            employee.setAge((Integer) empRaw.get("age"));
            employee.setDepartment((String) empRaw.get("department"));
            employee.setFirstName((String) empRaw.get("firstName"));
            employee.setLastName((String) empRaw.get("lastName"));
            employee.setIncome(new Money((String) empRaw.get("income")));
            employeeList.add(employee);
        }
        return employeeList;

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

    @Test
    void employeeAboveAge_Above29NotIncludingItWith3Results() throws MoneyProcessorException {
        Mockito.when(employeeRepository.findAllEmployee()).thenReturn(getEmployeeList_Initialized());
        ResponseEntity responseEntity = this.restTemplate.exchange("http://localhost:" + port + "/employee?ageAbove=29", HttpMethod.GET,
                new HttpEntity<>(new HashMap()),
                Map.class);
        Assert.assertEquals(200, responseEntity.getStatusCodeValue());
        Map<String, Object> data;
        data = (Map<String, Object>) responseEntity.getBody();
        System.out.println(data);
        Assert.assertTrue(data.containsKey("employees"));
        List<Employee> employeeResultList = getEmployeeListFromMapResponse(data);
        Assert.assertEquals(3, employeeResultList.size());
        Assert.assertEquals(0, employeeResultList.stream().filter(e -> (e.getAge() < 30)).collect(Collectors.toList()).size());
    }

    @Test
    void employeeAboveSalary_AmountWithNoCurrency() throws MoneyProcessorException {
        Mockito.when(employeeRepository.findAllEmployee()).thenReturn(getEmployeeList_Initialized());
        ResponseEntity responseEntity = this.restTemplate.exchange("http://localhost:" + port + "/employee?salaryAbove=2000", HttpMethod.GET,
                new HttpEntity<>(new HashMap()),
                Map.class);
        Assert.assertEquals(400, responseEntity.getStatusCodeValue());
        Map<String, Object> data;
        data = (Map<String, Object>) responseEntity.getBody();
        System.out.println(data);
        Assert.assertTrue(data.containsKey("error"));
    }

    @Test
    void employeeAboveSalary_AmountWithCurrencyButInvalidNumber() throws MoneyProcessorException {
        Mockito.when(employeeRepository.findAllEmployee()).thenReturn(getEmployeeList_Initialized());
        ResponseEntity responseEntity = this.restTemplate.exchange("http://localhost:" + port + "/employee?salaryAbove=$2A000", HttpMethod.GET,
                new HttpEntity<>(new HashMap()),
                Map.class);
        Assert.assertEquals(400, responseEntity.getStatusCodeValue());
        Map<String, Object> data;
        data = (Map<String, Object>) responseEntity.getBody();
        System.out.println(data);
        Assert.assertTrue(data.containsKey("error"));
    }

    @Test
    void employeeAboveSalary_validAmount() throws MoneyProcessorException {
        Mockito.when(employeeRepository.findAllEmployee()).thenReturn(getEmployeeList_Initialized());
        ResponseEntity responseEntity = this.restTemplate.exchange("http://localhost:" + port + "/employee?salaryAbove=$2000", HttpMethod.GET,
                new HttpEntity<>(new HashMap()),
                Map.class);
        Assert.assertEquals(200, responseEntity.getStatusCodeValue());
        Map<String, Object> data;
        data = (Map<String, Object>) responseEntity.getBody();
        System.out.println(data);
        Assert.assertTrue(data.containsKey("employees"));
        List<Employee> employeeResultList = getEmployeeListFromMapResponse(data);
        Assert.assertEquals(4, employeeResultList.size());
        Assert.assertEquals(0, employeeResultList.stream().filter(e ->
                e.getIncome().getAmount().compareTo(new BigDecimal("2000")) < 1 ? true : false)
                .collect(Collectors.toList()).size());

    }
}