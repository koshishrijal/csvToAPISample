package com.kosh.csvrestapi.data;

import com.kosh.csvrestapi.repository.EmployeeApplicationMemoryRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class EmployeeCollectionWriter implements ItemWriter<Employee> {
    @Override
    public void write(List<? extends Employee> list) throws Exception {
        EmployeeApplicationMemoryRepo.setEmployeeStore((List<Employee>) list);
        log.info("Employee stored in list as:{} ", list);
    }
}
