package com.example.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.employee.dto.EmployeeRequest;
import com.example.employee.dto.EmployeeResponse;
import com.example.employee.model.Employee;
import com.example.employee.payload.ApiResponse;
import com.example.employee.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(
    name = "Employee Management",
    description = "CRUD Operations for Employee"
)
@RestController
@RequestMapping("/employees")
public class EmployeeController {
  @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeResponse>>> getAllEmployees() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Employees found", employeeService.getAllEmployees()));
    }

//     @GetMapping("/{id}")
// public Employee getEmployeeById(@PathVariable int id) {

//     return employeeService.getEmployeeById(id);

// }

@Operation(summary = "Get employee by id")
@GetMapping("/{id}")
public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployeeById(@Parameter(description = "Employee Id")@PathVariable int id) {


    return ResponseEntity.ok(new ApiResponse<>(true, "Employee found", employeeService.getEmployeeById(id)));
}


//     @PostMapping
// public Employee addEmployee(@Valid @RequestBody Employee employee) {
//     return employeeService.addEmployee(employee);
// }

@Operation(summary = "Create Employee")
@PostMapping
public ResponseEntity<ApiResponse<EmployeeResponse>> addEmployee(
        @Valid @RequestBody EmployeeRequest request){

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new ApiResponse<>(true, "Employee created successfully", employeeService.addEmployee(request)));
}

@PutMapping("/{id}")
public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(
        @PathVariable int id,
        @RequestBody Employee employee) {

    return ResponseEntity.ok(new ApiResponse<>(true, "Employee updated successfully", employeeService.updateEmployee(id, employee)));
}

@DeleteMapping("/{id}")
public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable int id) {

    employeeService.deleteEmployee(id);
    return ResponseEntity.noContent().build();
}

@GetMapping("/name/{name}")
public ResponseEntity<ApiResponse<List<EmployeeResponse>>> findEmployeesByName(@PathVariable String name) {
    return ResponseEntity.ok(new ApiResponse<>(true, "Employees found", employeeService.findEmployeesByName(name)));
}

@GetMapping("/salary/{salary}")
public ResponseEntity<ApiResponse<List<EmployeeResponse>>> findEmployeesBySalary(@PathVariable double salary){
  return ResponseEntity.ok(new ApiResponse<>(true, "Employees found", employeeService.findEmployeesBySalary(salary)));
}

@GetMapping("/search")
public ResponseEntity<ApiResponse<List<EmployeeResponse>>> findEmployeesByNameAndSalary(
        @RequestParam String name,
        @RequestParam double salary) {
    return ResponseEntity.ok(new ApiResponse<>(true, "Employees found", employeeService.findEmployeesByNameAndSalary(name, salary)));
}

@GetMapping("/salary/greaterThan")
public ResponseEntity<ApiResponse<List<EmployeeResponse>>> findEmployeesWithSalaryGreaterThan(
        @RequestParam double salary) {
    return ResponseEntity.ok(new ApiResponse<>(true, "Employees found", employeeService.findEmployeesWithSalaryGreaterThan(salary)));
}

@GetMapping("/salary/between")
public ResponseEntity<ApiResponse<List<EmployeeResponse>>> findEmployeesWithSalaryBetween(
        @RequestParam double minSalary,
        @RequestParam double maxSalary) {
    return ResponseEntity.ok(new ApiResponse<>(true, "Employees found", employeeService.findEmployeesWithSalaryBetween(minSalary, maxSalary)));
}

@GetMapping("/name/containing")
public ResponseEntity<ApiResponse<List<EmployeeResponse>>> findEmployeesByNameContaining(
        @RequestParam String name) {
    return ResponseEntity.ok(new ApiResponse<>(true, "Employees found", employeeService.findEmployeesByNameContaining(name)));
}

@GetMapping("/count")
public ResponseEntity<ApiResponse<Long>> countEmployees() {
    return ResponseEntity.ok(new ApiResponse<>(true, "Employee count", employeeService.countEmployees()));
}

@GetMapping("/max-salary")
public ResponseEntity<ApiResponse<Double>> findMaxSalary() {
    return ResponseEntity.ok(new ApiResponse<>(true, "Max salary found", employeeService.findMaxSalary()));
}

@GetMapping("/paginated")
public ResponseEntity<ApiResponse<Page<Employee>>> getAllEmployeespaginated(Pageable pageable) {
    return ResponseEntity.ok(new ApiResponse<>(true, "Employees found", employeeService.getAllEmployeesPaginated(pageable)));
}

@GetMapping("/specificationSearch")
public ResponseEntity<ApiResponse<List<EmployeeResponse>>> searchEmployees(

        @RequestParam(required = false) String name,

        @RequestParam(required = false) Double salary,

        @RequestParam(required = false) String department) {

    return ResponseEntity.ok(new ApiResponse<>(true, "Employees found", employeeService.search(
            name,
            salary,
            department
    )));
}
}