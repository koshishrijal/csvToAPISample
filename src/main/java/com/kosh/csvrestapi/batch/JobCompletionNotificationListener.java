package com.kosh.csvrestapi.batch;


import com.kosh.csvrestapi.data.Employee;
import com.kosh.csvrestapi.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private final EmployeeRepository employeeRepository;

    public JobCompletionNotificationListener(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!Job Completed! verifying the results");
            List<Employee> employeeList = employeeRepository.findAllEmployee();
            int employeeListSize = employeeList.size();
            log.info("Employee List size:{}", employeeListSize);
        }
    }
}
