package com.example.employee.dto;

import lombok.Data;

@Data
public class EmployeeResponse {

      private Integer id;

    private String name;

    private double salary;

    private String departmentName;
}
