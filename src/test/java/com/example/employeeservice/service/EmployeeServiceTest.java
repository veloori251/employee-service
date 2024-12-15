package com.example.employeeservice.service;

import com.example.employeeservice.model.Employee;
import com.example.employeeservice.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    private Employee employee1;
    private Employee employee2;
    private Employee employee3;
    private Employee existingEmployee;
    private Employee updatedEmployee;

    @BeforeEach
    public void setUp(){
        employee1 = new Employee(1L,"Vamsi","Developer",45000.0);
        employee2 = new Employee(2L,"Krishna","Manager",65000.0);
        employee3 = new Employee(3L,"Bhargavi","Director",145000.0);
        existingEmployee = new Employee(4L,"Sai","Architect",150000.0);
        updatedEmployee = new Employee(4L,"sai","architect",160000.0);

    }

    @Test
    void testCreateEmployee(){
        when(employeeRepository.save(employee1)).thenReturn(employee1);
        Employee createdEmployee = employeeService.createEmployee(employee1);
        assertNotNull(createdEmployee);
        assertEquals(employee1.getName(),createdEmployee.getName());
        verify(employeeRepository,times(1)).save(employee1);
    }

    @Test
    void getEmployeeById(){
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee2));

        Employee foundEmployee = employeeService.findByEmployeeId(2L);
        assertNotNull(foundEmployee);
        assertEquals(employee2.getName(),foundEmployee.getName());
        verify(employeeRepository,times(1)).findById(2L);
    }

    @Test
    void testGetAllEmployees(){
        when(employeeRepository.findAll()).thenReturn(List.of(employee1,employee2,employee3));

        List<Employee> foundEmployees = employeeService.getAllEmployees();

        assertNotNull(foundEmployees);
        assertEquals(3,foundEmployees.size());

    }

    @Test
    void testGetAllEmployeesWithPaginationAndSorting(){
        List<Employee> employees = List.of(employee1,employee2,employee3);
        Page<Employee> employeePage = new PageImpl<>(employees);
        when(employeeRepository.findAll(PageRequest.of(0,10, Sort.by("name").descending()))).thenReturn(employeePage);

        Page<Employee> foundEmployees = employeeService.getEmployeeWithPaginationAndSorting(0,10,"name","desc");
        assertNotNull(foundEmployees);
        assertEquals(3,foundEmployees.getTotalElements());
        assertEquals(1,foundEmployees.getTotalPages());
        assertEquals(employee1.getName(),foundEmployees.getContent().get(0).getName());

    }

    @Test
    void testDeleteById(){
        when(employeeRepository.existsById(1L)).thenReturn(true);
        boolean isDeleted = employeeService.deleteEmployeeById(1L);
        assertTrue(isDeleted);
        verify(employeeRepository,times(1)).existsById(1L);
        verify(employeeRepository,times(1)).deleteById(1L);
    }

    @Test
    void testGetEmployeeId_NotFound(){
        when(employeeRepository.findById(100L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,() ->{
            employeeService.findByEmployeeId(100L);
        });

        assertEquals("Employee not found with id: 100",ex.getMessage());
    }

    @Test
    void testDeleteById_NotFound(){
        when(employeeRepository.existsById(100L)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> employeeService.deleteEmployeeById(100L));
    }

    @Test
    void testUpdateEmployee_exist(){
        when(employeeRepository.findById(4L)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        Employee updated = employeeService.updateEmployeeById(4L,updatedEmployee);
        assertNotNull(updated);
        assertEquals("sai",updated.getName());
        assertEquals("architect",updated.getDesignation());
        assertEquals(160000.0,updated.getSalary());
        verify(employeeRepository).findById(4L);

    }

    @Test
    void testUpdateEmployee_NotFound(){
        when(employeeRepository.findById(100L)).thenReturn(Optional.empty());
        ResponseStatusException ex = Assertions.assertThrows(ResponseStatusException.class, () ->{
            employeeService.updateEmployeeById(100L,updatedEmployee);
        });

        assertEquals(HttpStatus.NOT_FOUND,ex.getStatusCode());
        verify(employeeRepository).findById(100L);
        verify(employeeRepository,never()).save(any(Employee.class));

    }
}

