package com.example.springboottesting.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.springboottesting.exception.ResourceNotFoundException;
import com.example.springboottesting.model.Employee;
import com.example.springboottesting.repository.EmployeeRepository;
import com.example.springboottesting.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exist with given email" + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(long id, Employee updatedEmployeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist id: " + id));

        employee.setFirstName(updatedEmployeeDetails.getFirstName());
        employee.setLastName(updatedEmployeeDetails.getLastName());
        employee.setEmail(updatedEmployeeDetails.getEmail());

        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(long id) {
        System.out.println("Olaaaa");
        employeeRepository.deleteById(id);
    }

}
