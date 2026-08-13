package com.example.employee.controller;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


import com.example.employee.dto.EmployeeResponse;
import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.security.CustomUserDetailsService;
import com.example.employee.security.JwtService;
import com.example.employee.service.EmployeeService;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
private CustomUserDetailsService customUserDetailsService;

    @Test
    void testGetEmployeeById() throws Exception {
        
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setId(1);
        employeeResponse.setName("John Doe");
        employeeResponse.setSalary(50000.0);
        
        when(employeeService.getEmployeeById(1)).thenReturn(employeeResponse);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.salary").value(50000.0));
    }

    @Test
void testGetEmployeeById_WhenEmployeeNotFound() throws Exception {

    when(employeeService.getEmployeeById(1))
            .thenThrow(new EmployeeNotFoundException(
                    "Employee not found with id: 1"
            ));

    mockMvc.perform(get("/employees/1"))
            .andExpect(status().isNotFound());
}

@Test
void testDeleteEmployee() throws Exception {

    // Arrange
    doNothing().when(employeeService).deleteEmployee(1);

    // Act + Assert
    mockMvc.perform(delete("/employees/1"))
            .andExpect(status().isNoContent());

    // Verify
    verify(employeeService).deleteEmployee(1);
}
@Test
void testDeleteEmployee_WhenEmployeeNotFound() throws Exception {

    // Arrange
    doThrow(new EmployeeNotFoundException(
            "Employee not found with id: 1"
    ))
    .when(employeeService)
    .deleteEmployee(1);

    // Act + Assert
    mockMvc.perform(delete("/employees/1"))
            .andExpect(status().isNotFound());

    // Verify
    verify(employeeService).deleteEmployee(1);
}
}