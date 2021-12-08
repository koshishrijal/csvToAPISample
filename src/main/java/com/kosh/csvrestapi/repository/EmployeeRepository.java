package com.kosh.csvrestapi.repository;

import com.kosh.csvrestapi.data.Employee;

import java.util.List;

public interface EmployeeRepository {

     List<Employee> findAllEmployee();
}
