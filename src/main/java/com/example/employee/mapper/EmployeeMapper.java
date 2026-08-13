package com.example.employee.mapper;

import org.springframework.stereotype.Component;

import com.example.employee.dto.EmployeeRequest;
import com.example.employee.dto.EmployeeResponse;
import com.example.employee.model.Department;
import com.example.employee.model.Employee;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequest request,Department department) {
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setSalary(request.getSalary());
        employee.setDepartment(department);
        return employee;
    }


    public EmployeeResponse toResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setName(employee.getName());
        response.setSalary(employee.getSalary());
        response.setDepartmentName(employee.getDepartment().getName());
        return response;
    }
}
