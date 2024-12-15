package com.example.employeeservice.controller;

import com.example.employeeservice.model.Employee;
import com.example.employeeservice.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/create")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee){
       return new ResponseEntity<>(employeeService.createEmployee(employee), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable long id){
        try{
            return ResponseEntity.ok(employeeService.findByEmployeeId(id));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Employee>> getAllEmployees(){
        return ResponseEntity.ok().body(employeeService.getAllEmployees());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Employee> updateEmployeeById(@PathVariable long id, @RequestBody Employee employee){
        return ResponseEntity.ok().body(employeeService.updateEmployeeById(id,employee));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployeeByID(@PathVariable long id){
        try{
            boolean isDeleted = employeeService.deleteEmployeeById(id);
            if(isDeleted)
                return ResponseEntity.ok("Employee deleted successfully!");
            return ResponseEntity.status(400).body("Error in deletion");
        }catch (ResponseStatusException e){
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @GetMapping("/paged")
    public ResponseEntity<Map<String , Object>> getEmployeesWithPaginationAndSorting(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir){

        Page<Employee> pageEmployees = employeeService.getEmployeeWithPaginationAndSorting(page,size,sortBy,sortDir);

        Map<String,Object> response = new HashMap<>();
        response.put("employees",pageEmployees.getContent());
        response.put("currentPage",pageEmployees.getNumber());
        response.put("totalItems",pageEmployees.getTotalElements());
        response.put("totalPages",pageEmployees.getTotalPages());
        response.put("isFirstPage",pageEmployees.isFirst());
        response.put("isLastPage", pageEmployees.isLast());
        response.put("pageSize",pageEmployees.getSize());
        response.put("numberOfElements",pageEmployees.getNumberOfElements());

        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}
