package com.kosh.csvrestapi.data;

import com.kosh.csvrestapi.repository.EmployeeRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeApplicationMemoryRepo implements EmployeeRepository {

    private static List<Employee> EMPLOYEE_STORE = new ArrayList<>();

    public static void setEmployeeStore(List<Employee> employeeStore) {
        EMPLOYEE_STORE.addAll(employeeStore);
    }

    @Override
    public List<Employee> findAllEmployee() {
        return EMPLOYEE_STORE;
    }
}
