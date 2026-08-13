package com.example.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

}
