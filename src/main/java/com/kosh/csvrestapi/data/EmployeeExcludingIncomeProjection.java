package com.kosh.csvrestapi.data;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public interface EmployeeExcludingIncomeProjection {


    public String getFirstName();

    public String getLastName();

    public int getAge();

    public String getDepartment();
}
