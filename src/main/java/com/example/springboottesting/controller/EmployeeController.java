package com.example.springboottesting.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboottesting.model.Employee;
import com.example.springboottesting.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

  private EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Employee createEmployee(@RequestBody Employee employee) {
    return employeeService.saveEmployee(employee);
  }

  @GetMapping
  public List<Employee> getAllEmployees() {
    return employeeService.getAllEmployees();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
    return employeeService.getEmployeeById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Employee> updateEmployee(@PathVariable long id, @RequestBody Employee employeeDetails) {
    Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
    return ResponseEntity.ok(updatedEmployee);

    // return employeeService.getEmployeeById(employeeId)
    // .map(savedEmployee -> {
    // savedEmployee.setFirstName(employee.getFirstName());
    // savedEmployee.setLastName(employee.getLastName());
    // savedEmployee.setEmail(employee.getEmail());

    // Employee updatedEmployee = employeeService.updateEmployee(savedEmployee);
    // return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    // })
    // .orElseGet(() -> ResponseEntity.notFound().build());
    // }

  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
    System.out.println("Olaaaa");
    employeeService.deleteEmployee(id);
    return new ResponseEntity<String>("Employee deleted successfully!", HttpStatus.OK);
  }
}
