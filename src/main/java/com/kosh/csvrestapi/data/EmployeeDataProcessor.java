package com.kosh.csvrestapi.data;

import com.kosh.csvrestapi.exception.MoneyProcessorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class EmployeeDataProcessor implements ItemProcessor<EmployeeInput, Employee> {


    @Override
    public Employee process(final EmployeeInput employeeInput) throws MoneyProcessorException {
        String incomeStr = employeeInput.getIncome();
        // try {
        final Employee employee = new Employee(employeeInput.getFirstName(), employeeInput.getLastName(), employeeInput.getAge(), new Money(incomeStr), employeeInput.getDepartment());
        log.info("Converting (" + employeeInput + ") into (" + employee + ")");
        return employee;
//        } catch (MoneyProcessorException e) {
//            log.error("Error converting ( " + employeeInput + " )" + e.getMessage());
//            return null;
//        }
    }

}