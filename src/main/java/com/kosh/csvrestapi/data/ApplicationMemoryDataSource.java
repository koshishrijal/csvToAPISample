package com.kosh.csvrestapi.data;

import java.util.ArrayList;
import java.util.List;

public class ApplicationMemoryDataSource {
    private static List<Employee> EMPLOYEE_STORE = new ArrayList<>();

    public static List<Employee> getEmployeeStore() {
        return EMPLOYEE_STORE;
    }

    public static void setEmployeeStore(List<Employee> employeeStore) {
        EMPLOYEE_STORE.addAll(employeeStore);
    }
}
