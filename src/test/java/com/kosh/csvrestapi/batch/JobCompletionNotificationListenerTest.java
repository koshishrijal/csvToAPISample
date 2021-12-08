package com.kosh.csvrestapi.batch;

import com.kosh.csvrestapi.data.Employee;
import com.kosh.csvrestapi.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JobCompletionNotificationListenerTest {

    @Mock
    private EmployeeRepository employeeRepository;

    private JobCompletionNotificationListener jobCompletionNotificationListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.jobCompletionNotificationListener = new JobCompletionNotificationListener(employeeRepository);
    }

    @Test
    void afterJob() {
        JobExecution jobExecution = new JobExecution(1l);
        jobExecution.setStatus(BatchStatus.COMPLETED);
        Mockito.when(employeeRepository.findAllEmployee()).thenReturn(getEmployeeListSize_0());
        Assertions.assertDoesNotThrow(() -> {
            this.jobCompletionNotificationListener.afterJob(jobExecution);
        });
    }

    public static List<Employee> getEmployeeListSize_2() {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee());
        employeeList.add(new Employee());
        return employeeList;
    }

    public static List<Employee> getEmployeeListSize_0() {
        return new ArrayList<>();
    }
}