package com.example.employeeservice.config;

import com.example.employeeservice.model.Employee;
import com.example.employeeservice.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@Configuration
@AllArgsConstructor
public class DataInitializer {

    private final EmployeeRepository employeeRepository;

    @Bean
    public ApplicationRunner initializer(){
        return args -> {
            employeeRepository.save(new Employee(1L,"Vamsi","Developer",34000.00));
            employeeRepository.save(new Employee(2L,"Krishna","Manager",84000.00));
            employeeRepository.save(new Employee(3L,"Veloori","Architect",94000.00));
            employeeRepository.save(new Employee(4L,"Sai","senior Analyst",44000.00));
            employeeRepository.save(new Employee(5L,"Bhargavi","Tech Lead",54000.00));
            employeeRepository.save(new Employee(6L,"Vidya","Consultant",78000.00));
            employeeRepository.save(new Employee(7L,"Supriya","IT Analyst",41000.00));
            employeeRepository.save(new Employee(8L,"Bavajan","Ass System Engineer",54000.00));
            employeeRepository.save(new Employee(9L,"Charan","System Engineer",64000.00));
            employeeRepository.save(new Employee(10L,"Nagamani","Director",134000.00));
        };
    }

}
