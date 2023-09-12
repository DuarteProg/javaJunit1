package com.example.springboottesting.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.springboottesting.model.Employee;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("Lucas")
                .lastName("Barbosa")
                .email("lucas@hotmail.com").build();
    }

    @Test
    public void givenEmployeeObject_when_Save_thenReturnSavedEmployee() {

        Employee savedEmployee = employeeRepository.save(employee);

        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);

    }

    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList() {

        Employee employee1 = Employee.builder()
                .firstName("Gabriela")
                .lastName("Barbosa")
                .email("gabriela@hotmail.com").build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        List<Employee> employeeList = employeeRepository.findAll();

        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

    }

    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {

        employeeRepository.save(employee);
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        assertThat(employeeDB).isNotNull();

    }

    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {

        employeeRepository.save(employee);

        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        assertThat(employeeDB).isNotNull();
    }

    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() {

        employeeRepository.save(employee);

        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("luke@hotmail.com");
        savedEmployee.setFirstName("luke");
        savedEmployee.setLastName("barbin");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        assertThat(updatedEmployee.getEmail()).isEqualTo("luke@hotmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("luke");
        assertThat(updatedEmployee.getLastName()).isEqualTo("barbin");
    }

    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {

        employeeRepository.save(employee);

        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        assertThat(employeeOptional).isEmpty();

    }

    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {

        employeeRepository.save(employee);

        String firstName = "Lucas";
        String lastName = "Barbosa";

        Employee savEmployee = employeeRepository.findByJPQLEmployee(firstName, lastName);

        assertThat(savEmployee).isNotNull();
    }

}
