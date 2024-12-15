package com.example.employeeservice.service;

import com.example.employeeservice.model.Employee;
import com.example.employeeservice.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;

    public Employee createEmployee(Employee employee){
        return repository.save(employee);
    }

    public Employee findByEmployeeId(long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found with id: "+id));
    }

    public List<Employee> getAllEmployees(){
        return repository.findAll();
    }

    public boolean deleteEmployeeById(long id){
        if(!repository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee not found");
        }
        repository.deleteById(id);
        return true;
    }

    public Employee updateEmployeeById(long id,Employee employee){
        Employee currentEmployee = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Employee not found with id: +id"));
        currentEmployee.setName(employee.getName());
        currentEmployee.setDesignation(employee.getDesignation());
        currentEmployee.setSalary(employee.getSalary());
        return repository.save(currentEmployee);
    }

    public Page<Employee> getEmployeeWithPaginationAndSorting(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size,sort);
        return repository.findAll(pageable);
    }


}
