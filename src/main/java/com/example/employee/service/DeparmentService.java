package com.example.employee.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.employee.model.Department;
import com.example.employee.repository.DepartmentRepository;

@Service
public class DeparmentService {

    private final DepartmentRepository departmentRepository;

    public DeparmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department addDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(int id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }
}
