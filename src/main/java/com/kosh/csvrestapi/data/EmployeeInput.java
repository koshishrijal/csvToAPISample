package com.kosh.csvrestapi.data;

import lombok.Data;

@Data
public class EmployeeInput {
    private String firstName;
    private String lastName;
    private int age;
    private String income;
    private String department;
}
