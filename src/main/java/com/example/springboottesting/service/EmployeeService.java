package com.example.springboottesting.service;

import java.util.List;
import java.util.Optional;

import com.example.springboottesting.model.Employee;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Optional<Employee> getEmployeeById(Long id);

    Employee updateEmployee(long id, Employee updatedEmployee);

    void deleteEmployee(long id);
}
