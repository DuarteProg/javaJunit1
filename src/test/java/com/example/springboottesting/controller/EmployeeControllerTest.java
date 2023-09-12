package com.example.springboottesting.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.springboottesting.exception.ResourceNotFoundException;
import com.example.springboottesting.model.Employee;
import com.example.springboottesting.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class EmployeeControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private EmployeeService employeeService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void givenEmployeeObject_whenCreateEmployee_thenSavedEmployee() throws Exception {

                Employee employee = Employee.builder()
                                .firstName("Lucas")
                                .lastName("Barbosa")
                                .email("lucas@hotmail.com")
                                .build();

                BDDMockito.given(employeeService
                                .saveEmployee(ArgumentMatchers.any(Employee.class)))
                                .willAnswer((invocation) -> invocation.getArgument(0));

                ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                                .post("/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(employee)));

                response.andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.status().isCreated())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                                                CoreMatchers.is(employee.getFirstName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                                                CoreMatchers.is(employee.getLastName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                                                CoreMatchers.is(employee.getEmail())));
        }

        @Test
        public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {

                List<Employee> listOfEmployees = new ArrayList<>();
                listOfEmployees.add(Employee.builder().id(0)
                                .firstName("Lucas").lastName("Barbosa")
                                .email("lucas@hotmail.com").build());
                listOfEmployees.add(Employee.builder().id(1)
                                .firstName("Gabriela").lastName("Barbosa")
                                .email("gabriela@hotmail.com").build());

                BDDMockito.given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

                ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                                .get("/employees"));

                response
                                .andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                                                CoreMatchers.is(listOfEmployees.size())));
        }

        @Test
        public void givenEmployeeId_thenGetEmployeeById_returnEmployeeObject() throws Exception {

                long employeeId = 1L;
                Employee employee = Employee.builder()
                                .firstName("Lucas")
                                .lastName("Barbosa")
                                .email("lucas@hotmail.com")
                                .build();

                BDDMockito.given(employeeService.getEmployeeById(employeeId))
                                .willReturn(Optional.of(employee));

                ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                                .get("/employees/{id}", employeeId));

                response.andExpect(MockMvcResultMatchers.status().isOk())
                                .andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                                                CoreMatchers.is(employee.getFirstName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                                                CoreMatchers.is(employee.getLastName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                                                CoreMatchers.is(employee.getEmail())));
        }

        @Test
        public void givenInvalidEmployeeId_thenGetEmployeeById_thenReturnEmpty() throws Exception {

                long employeeId = 1L;

                BDDMockito.given(employeeService.getEmployeeById(employeeId))
                                .willReturn(Optional.empty());

                ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                                .get("/employees/{id}", employeeId));

                response.andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void givenUpdatedEmployee_whenUpdateEmployee_theReturnUpdateEmployeeObject() throws Exception {
                long employeeId = 1L;
                Employee savedEmployee = Employee.builder()
                                .firstName("Lucas")
                                .lastName("Barbosa")
                                .email("lucas@hotmail.com")
                                .build();

                Employee updatedEmployee = Employee.builder()
                                .firstName("Gabi")
                                .lastName("Barbosa")
                                .email("gabi@hotmail.com")
                                .build();

                // Configure o comportamento específico do método updateEmployee usando matchers
                BDDMockito.given(employeeService.getEmployeeById(employeeId))
                                .willReturn(Optional.of(savedEmployee));

                BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.eq(employeeId),
                                ArgumentMatchers.any(Employee.class)))
                                .willAnswer((invocation) -> {
                                        Employee employeeToUpdate = invocation.getArgument(1);
                                        savedEmployee.setFirstName(employeeToUpdate.getFirstName());
                                        savedEmployee.setLastName(employeeToUpdate.getLastName());
                                        savedEmployee.setEmail(employeeToUpdate.getEmail());
                                        return savedEmployee;
                                });

                ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                                .put("/employees/{id}", employeeId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedEmployee)));

                response.andExpect(MockMvcResultMatchers.status().isOk())
                                .andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                                                CoreMatchers.is(updatedEmployee.getFirstName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                                                CoreMatchers.is(updatedEmployee.getLastName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                                                CoreMatchers.is(updatedEmployee.getEmail())));
        }

        @Test
        public void givenUpdatedEmployee_whenUpdateEmployee_theReturn404() throws Exception {
                long employeeId = 1L;

                Employee updatedEmployee = Employee.builder()
                                .firstName("Gabi")
                                .lastName("Barbosa")
                                .email("gabi@hotmail.com")
                                .build();

                // Configure o comportamento específico do método updateEmployee usando matchers
                BDDMockito.given(employeeService.getEmployeeById(employeeId))
                                .willReturn(Optional.empty());

                BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.eq(employeeId),
                                ArgumentMatchers.any(Employee.class)))
                                .willThrow(new ResourceNotFoundException("Employee not exist id: " + employeeId));

                ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                                .put("/employees/{id}", employeeId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedEmployee)));

                response.andExpect(MockMvcResultMatchers.status().isNotFound())
                                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
                long employeeId = 1L;
                Mockito.doNothing().when(employeeService).deleteEmployee(employeeId);

                ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                                .delete("/employees/{id}", employeeId));

                response.andExpect(MockMvcResultMatchers.status().isOk())
                                .andDo(MockMvcResultHandlers.print());

        }
}
