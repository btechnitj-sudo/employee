package com.example.employee.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.model.Department;
import com.example.employee.service.DeparmentService;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DeparmentService deparmentService;

    public DepartmentController(DeparmentService deparmentService) {
        this.deparmentService = deparmentService;
    }

    @PostMapping
    public ResponseEntity<Department> addDepartment(@RequestBody Department department) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deparmentService.addDepartment(department));
    }

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(deparmentService.getAllDepartments());
    }   

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable int id) {
        return ResponseEntity.ok(deparmentService.getDepartmentById(id));
    }
}
