package com.kosh.csvrestapi.service;

import com.kosh.csvrestapi.data.Employee;
import com.kosh.csvrestapi.data.Money;
import com.kosh.csvrestapi.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private List<Employee> allEmployee() {
        return this.employeeRepository.findAllEmployee();
    }

    public List<Employee> findByDepartment(String department) {
        return allEmployee().stream().filter(e -> e.getDepartment().equals(department)).collect(Collectors.toList());
    }

    public List<Employee> findAboveAge(int age) {
        return allEmployee().stream().filter(e -> e.getAge() > age).collect(Collectors.toList());
    }

    //TODO- handle for multiple currencies using third party api
    public List<Employee> findAboveSalary(Money money) {
        return allEmployee().stream().filter(e -> e.getIncome().getAmount().compareTo(money.getAmount()) > 0).collect(Collectors.toList());
    }
}
