package com.example.employee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeRequest {
    
  @NotBlank(message = "Name cannot be blank")
    private String name;

    @Positive(message = "Salary must be positive")
    private double salary;

    @NotNull(message = "Department is required")
    private Integer departmentId;
}
