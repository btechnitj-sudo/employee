package com.example.employee.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.employee.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>,JpaSpecificationExecutor<Employee> {

    List<Employee> findByName(String name);
    List<Employee> findBySalary(double salary);
    List<Employee> findByNameAndSalary(String name, double salary);
    List<Employee> findBySalaryGreaterThan(double salary);
    List<Employee> findBySalaryBetween(double minSalary, double maxSalary);
    List<Employee> findByNameContaining(String name);
    @Query("SELECT COUNT(e) FROM Employee e")
    long countEmployees();
    @Query("SELECT MAX(e.salary) FROM Employee e")
    double findMaxSalary();
    
}
