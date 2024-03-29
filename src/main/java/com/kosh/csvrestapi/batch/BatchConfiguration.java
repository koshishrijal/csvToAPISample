package com.kosh.csvrestapi.batch;

import com.kosh.csvrestapi.data.Employee;
import com.kosh.csvrestapi.data.EmployeeCollectionWriter;
import com.kosh.csvrestapi.data.EmployeeDataProcessor;
import com.kosh.csvrestapi.data.EmployeeInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {

    private final String[] EMPLOYEE_FIELD_NAMES = new String[]{"firstName", "lastName", "age", "income", "department"};

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Value("${employeeFile.name}")
    private String employeeCsvFile;

    @Bean
    public FlatFileItemReader<EmployeeInput> reader() {
        return new FlatFileItemReaderBuilder<EmployeeInput>()
                .linesToSkip(0)
                .name("employeeItemReader")
                .resource(new ClassPathResource(employeeCsvFile))
                .delimited().delimiter(",")
                .names(EMPLOYEE_FIELD_NAMES)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<EmployeeInput>() {{
                    setTargetType(EmployeeInput.class);
                }})
                .build();
    }

    @Bean
    public EmployeeDataProcessor processor() {
        return new EmployeeDataProcessor();
    }

    @Bean
    public EmployeeCollectionWriter writer() {
        return new EmployeeCollectionWriter();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step csvToJavaStep) {
        return jobBuilderFactory.get("employeeCsvToJavaJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(csvToJavaStep)
                .end()
                .build();
    }

    @Bean
    public Step csvToJavaStep(EmployeeCollectionWriter writer) {
        return stepBuilderFactory.get("csvToJavaStep")
                .<EmployeeInput, Employee>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(50)
                .skipPolicy((throwable, i) -> {
                    log.error("====Encountered Error=====:{} ", throwable.getMessage());
                    return true;
                })
                .build();
    }


}