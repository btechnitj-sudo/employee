package com.example.employee.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.employee.dto.EmployeeRequest;
import com.example.employee.dto.EmployeeResponse;
import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.mapper.EmployeeMapper;
import com.example.employee.model.Department;
import com.example.employee.model.Employee;
import com.example.employee.repository.DepartmentRepository;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.specification.EmployeeSpecification;

@Service
public class EmployeeService {

// private List<Employee> employees = new ArrayList<>();

private final EmployeeRepository employeeRepository;
 
private final DepartmentRepository departmentRepository;

private final EmployeeMapper employeeMapper;

public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, EmployeeMapper employeeMapper) {
    this.employeeRepository = employeeRepository;
    this.departmentRepository = departmentRepository;
    this.employeeMapper = employeeMapper;
}


public List<EmployeeResponse> getAllEmployees() {
       return employeeRepository.findAll().stream()
               .map(employeeMapper::toResponse)
               .toList();
    }

    public EmployeeResponse getEmployeeById(int id) {
         Employee employee = employeeRepository.findById(id)
            .orElseThrow(() ->
                    new EmployeeNotFoundException("Employee not found with id: " + id));

    return employeeMapper.toResponse(employee); 
    }
public EmployeeResponse addEmployee(EmployeeRequest request) {
    Department department = departmentRepository.findById(request.getDepartmentId())
            .orElseThrow(() -> new IllegalArgumentException("Department not found with id: " + request.getDepartmentId()));
    Employee employee = employeeMapper.toEntity(request, department);

    Employee savedEmployee = employeeRepository.save(employee);

    return employeeMapper.toResponse(savedEmployee);
}

public EmployeeResponse updateEmployee(int id, Employee updatedEmployee) {
    Employee existingEmployee = employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

    existingEmployee.setName(updatedEmployee.getName());
    existingEmployee.setSalary(updatedEmployee.getSalary());

    return employeeMapper.toResponse(employeeRepository.save(existingEmployee));
}

public void deleteEmployee(int id) {
    Employee existingEmployee = employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

    employeeRepository.delete(existingEmployee);
}

public List<EmployeeResponse> findEmployeesByName(String name) {
    return employeeRepository.findByName(name).stream()
            .map(employeeMapper::toResponse)
            .toList();
}

public List<EmployeeResponse> findEmployeesBySalary(double salary){
    return employeeRepository.findBySalary(salary).stream()
            .map(employeeMapper::toResponse)
            .toList();
}

public List<EmployeeResponse> findEmployeesByNameAndSalary(String name, double salary) {
    return employeeRepository.findByNameAndSalary(name, salary).stream()
            .map(employeeMapper::toResponse)
            .toList();
}

public List<EmployeeResponse> findEmployeesWithSalaryGreaterThan(double salary) {
    return employeeRepository.findBySalaryGreaterThan(salary).stream()
            .map(employeeMapper::toResponse)
            .toList();
}

public List<EmployeeResponse> findEmployeesWithSalaryBetween(double minSalary, double maxSalary) {
    return employeeRepository.findBySalaryBetween(minSalary, maxSalary).stream()
            .map(employeeMapper::toResponse)
            .toList();
}
 public List<EmployeeResponse> findEmployeesByNameContaining(String name) {
    return employeeRepository.findByNameContaining(name).stream()
            .map(employeeMapper::toResponse)
            .toList();
}

public Long countEmployees() {
    return employeeRepository.countEmployees();
}

public double findMaxSalary() {
    return employeeRepository.findMaxSalary();
}

public Page<Employee> getAllEmployeesPaginated(Pageable pageable) {
    return employeeRepository.findAll(pageable);
}
    
public List<EmployeeResponse> search(
            String name,
            Double salary,
            String department) {

        Specification<Employee> spec =
                Specification.where(EmployeeSpecification.hasName(name))
                             .and(EmployeeSpecification.hasSalaryGreaterThan(salary))
                             .and(EmployeeSpecification.hasDepartment(department));

        return employeeRepository.findAll(spec).stream()
                .map(employeeMapper::toResponse)
                .toList();
    }
}