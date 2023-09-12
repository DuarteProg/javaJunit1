package com.example.springboottesting.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.springboottesting.exception.ResourceNotFoundException;
import com.example.springboottesting.model.Employee;
import com.example.springboottesting.repository.EmployeeRepository;
import com.example.springboottesting.service.impl.EmployeeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        // employeeRepository = Mockito.mock(EmployeeRepository.class);
        // employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .id(1L)
                .firstName("Lucas")
                .lastName("Barbosa")
                .email("lucas@hotmail.com").build();

    }

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee))
                .willReturn(employee);

        Employee savedEmployee = employeeService.saveEmployee(employee);

        assertThat(savedEmployee).isNotNull();
    }

    @Test
    public void givenExistEmail_whenSaveEmployee_thenThrowsexception() {

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeeList() {

        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Gabriela")
                .lastName("Barbosa")
                .email("gabriela@hotmail.com").build();

        given(employeeRepository.findAll())
                .willReturn(List.of(employee, employee1));

        List<Employee> employeeList = employeeService.getAllEmployees();

        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeeList() {

        Employee.builder()
                .id(2L)
                .firstName("Gabriela")
                .lastName("Barbosa")
                .email("gabriela@hotmail.com").build();

        given(employeeRepository.findAll())
                .willReturn(Collections.emptyList());

        List<Employee> employeeList = employeeService.getAllEmployees();

        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {

        given(employeeRepository.findById(employee.getId()))
                .willReturn(Optional.of(employee));

        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        assertThat(savedEmployee).isNotNull();
    }

    @Test
public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
    // Suponha que você tenha um ID de funcionário válido, por exemplo, 1L
    long employeeId = 1L;

    // Defina o ID do funcionário no objeto employee
    employee.setId(employeeId);

    // Configure o comportamento do employeeRepository para retornar um Optional contendo o funcionário
    given(employeeRepository.findById(employeeId))
            .willReturn(Optional.of(employee));

    given(employeeRepository.save(employee))
            .willReturn((employee));

    employee.setEmail("Gabriela@hotmail.com");
    employee.setFirstName("Gabriela");

    // Atualize o método para passar o ID e os detalhes do funcionário
    Employee updatedEmployee = employeeService.updateEmployee(employeeId, employee);

    assertThat(updatedEmployee.getEmail()).isEqualTo("Gabriela@hotmail.com");
    assertThat(updatedEmployee.getFirstName()).isEqualTo("Gabriela");
    
    
}
    
    
    
    

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {

        BDDMockito.willDoNothing().given(employeeRepository)
                .deleteById(employee.getId());

        employeeService.deleteEmployee(employee.getId());

        verify(employeeRepository, times(1))
                .deleteById(employee.getId());
        ;

    }
}