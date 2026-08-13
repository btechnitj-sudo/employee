package com.example.employee.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import com.example.employee.dto.EmployeeResponse;
import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.mapper.EmployeeMapper;
import com.example.employee.model.Employee;
import com.example.employee.repository.DepartmentRepository;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.dto.EmployeeRequest;
import com.example.employee.model.Department;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void testGetEmployeeById() {
    Employee employee = new Employee();
    employee.setId(1);
    employee.setName("John Doe");
    employee.setSalary(50000.0);

    EmployeeResponse employeeResponse = new EmployeeResponse();
    employeeResponse.setId(1);
    employeeResponse.setName("John Doe");
    employeeResponse.setSalary(50000.0);

    when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
    when(employeeMapper.toResponse(employee)).thenReturn(employeeResponse);

    EmployeeResponse result = employeeService.getEmployeeById(1);



    assertEquals("John Doe", result.getName());
    assertEquals(50000.0, result.getSalary());

    verify(employeeRepository).findById(1);
    verify(employeeMapper).toResponse(employee);
}

@Test
void testDeleteEmployee() {

    // Arrange
    Employee employee = new Employee();
    employee.setId(1);
    employee.setName("John Doe");
    employee.setSalary(50000.0);

    when(employeeRepository.findById(1))
            .thenReturn(Optional.of(employee));

    // Act
    employeeService.deleteEmployee(1);

    // Verify
    verify(employeeRepository).findById(1);
    verify(employeeRepository).delete(employee);
}

@Test
void testDeleteEmployee_WhenEmployeeNotFound() {

    // Arrange
    when(employeeRepository.findById(1))
            .thenReturn(Optional.empty());

    // Act + Assert
    assertThrows(
            EmployeeNotFoundException.class,
            () -> employeeService.deleteEmployee(1)
    );

    // Verify
    verify(employeeRepository).findById(1);

    // delete should NOT be called
    verify(employeeRepository, never())
            .delete(any(Employee.class));
}

@Test
void testAddEmployee(){
        //Arrange
        EmployeeRequest request = new EmployeeRequest();
        request.setDepartmentId(1);
       Department department = new Department();
       department.setId(1);
       department.setName("IT");

        Employee employee = new Employee();
         employee.setId(1);
         employee.setName("John Doe");
         employee.setSalary(50000.0);

         Employee savedEmployee=new Employee();
         savedEmployee.setId(1);
         savedEmployee.setName("John Doe");
         savedEmployee.setSalary(50000.0);

        
         EmployeeResponse response = new EmployeeResponse();
         response.setId(1);
         response.setName("John Doe");
         response.setSalary(50000.0);
        
         when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
         when(employeeMapper.toEntity(request, department)).thenReturn(employee);
         when(employeeRepository.save(employee)).thenReturn(savedEmployee);
         when(employeeMapper.toResponse(savedEmployee)).thenReturn(response);

         //Act
                EmployeeResponse result = employeeService.addEmployee(request);

                assertEquals("John Doe", result.getName());
                assertEquals(50000.0, result.getSalary());

                verify(departmentRepository).findById(1);
                verify(employeeMapper).toEntity(request, department);
                verify(employeeRepository).save(employee);
                verify(employeeMapper).toResponse(savedEmployee);
}

@Test
void testAddEmployee_WhenDepartmentNotFound() {

    // Arrange
    EmployeeRequest request = new EmployeeRequest();
    request.setDepartmentId(1);

    when(departmentRepository.findById(1))
            .thenReturn(Optional.empty());

    // Act + Assert
    IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> employeeService.addEmployee(request)
    );

    // Verify exception message
    assertEquals(
            "Department not found with id: 1",
            exception.getMessage()
    );

    // Verify department was searched
    verify(departmentRepository).findById(1);

    // These should NOT be called
    verify(employeeMapper, never()).toEntity(any(), any());
    verify(employeeRepository, never()).save(any(Employee.class));
}

@Test
void testUpdateEmployee() {

    // Arrange

    Employee existingEmployee = new Employee();
    existingEmployee.setId(1);
    existingEmployee.setName("John Doe");
    existingEmployee.setSalary(50000.0);

    Employee updatedEmployee = new Employee();
    updatedEmployee.setName("John Smith");
    updatedEmployee.setSalary(60000.0);

    EmployeeResponse response = new EmployeeResponse();
    response.setId(1);
    response.setName("John Smith");
    response.setSalary(60000.0);

    when(employeeRepository.findById(1))
            .thenReturn(Optional.of(existingEmployee));

    when(employeeRepository.save(existingEmployee))
            .thenReturn(existingEmployee);

    when(employeeMapper.toResponse(existingEmployee))
            .thenReturn(response);

    // Act

    EmployeeResponse result =
            employeeService.updateEmployee(1, updatedEmployee);

    // Assert

    assertEquals("John Smith", result.getName());
    assertEquals(60000.0, result.getSalary());

    // Verify

    verify(employeeRepository).findById(1);
    verify(employeeRepository).save(existingEmployee);
    verify(employeeMapper).toResponse(existingEmployee);
}

@Test
void testUpdateEmployee_WhenEmployeeNotFound() {

    // Arrange

    Employee updatedEmployee = new Employee();
    updatedEmployee.setName("John Smith");
    updatedEmployee.setSalary(60000.0);

    when(employeeRepository.findById(1))
            .thenReturn(Optional.empty());

    // Act + Assert

    assertThrows(
            EmployeeNotFoundException.class,
            () -> employeeService.updateEmployee(1, updatedEmployee)
    );

    // Verify

    verify(employeeRepository).findById(1);

    verify(employeeRepository, never())
            .save(any(Employee.class));

    verify(employeeMapper, never())
            .toResponse(any(Employee.class));
}
@Test
void testGetAllEmployees() {

    // Arrange

    Employee employee1 = new Employee();
    employee1.setId(1);
    employee1.setName("John");
    employee1.setSalary(50000.0);

    Employee employee2 = new Employee();
    employee2.setId(2);
    employee2.setName("David");
    employee2.setSalary(60000.0);

    EmployeeResponse response1 = new EmployeeResponse();
    response1.setId(1);
    response1.setName("John");
    response1.setSalary(50000.0);

    EmployeeResponse response2 = new EmployeeResponse();
    response2.setId(2);
    response2.setName("David");
    response2.setSalary(60000.0);

    when(employeeRepository.findAll())
            .thenReturn(List.of(employee1, employee2));

    when(employeeMapper.toResponse(employee1))
            .thenReturn(response1);

    when(employeeMapper.toResponse(employee2))
            .thenReturn(response2);

    // Act

    List<EmployeeResponse> result =
            employeeService.getAllEmployees();

    // Assert

    assertEquals(2, result.size());

    assertEquals("John", result.get(0).getName());
    assertEquals(50000.0, result.get(0).getSalary());

    assertEquals("David", result.get(1).getName());
    assertEquals(60000.0, result.get(1).getSalary());

    // Verify

    verify(employeeRepository).findAll();

    verify(employeeMapper).toResponse(employee1);
    verify(employeeMapper).toResponse(employee2);
}
@Test
void testGetAllEmployees_WhenNoEmployees() {

    // Arrange

    when(employeeRepository.findAll())
            .thenReturn(List.of());

    // Act

    List<EmployeeResponse> result =
            employeeService.getAllEmployees();

    // Assert

    assertEquals(0, result.size());

    // Verify

    verify(employeeRepository).findAll();

    verify(employeeMapper, never())
            .toResponse(any(Employee.class));
}
@Test
void testFindEmployeesByName() {

    // Arrange
    Employee employee1 = new Employee();
    employee1.setId(1);
    employee1.setName("John");
    employee1.setSalary(50000.0);

    Employee employee2 = new Employee();
    employee2.setId(2);
    employee2.setName("John");
    employee2.setSalary(60000.0);

    EmployeeResponse response1 = new EmployeeResponse();
    response1.setId(1);
    response1.setName("John");
    response1.setSalary(50000.0);

    EmployeeResponse response2 = new EmployeeResponse();
    response2.setId(2);
    response2.setName("John");
    response2.setSalary(60000.0);

    when(employeeRepository.findByName("John"))
            .thenReturn(List.of(employee1, employee2));

    when(employeeMapper.toResponse(employee1))
            .thenReturn(response1);

    when(employeeMapper.toResponse(employee2))
            .thenReturn(response2);

    // Act
    List<EmployeeResponse> result =
            employeeService.findEmployeesByName("John");

    // Assert
    assertEquals(2, result.size());

    assertEquals("John", result.get(0).getName());
    assertEquals(50000.0, result.get(0).getSalary());

    assertEquals("John", result.get(1).getName());
    assertEquals(60000.0, result.get(1).getSalary());

    // Verify
    verify(employeeRepository).findByName("John");

    verify(employeeMapper).toResponse(employee1);
    verify(employeeMapper).toResponse(employee2);
}
@Test
void testFindEmployeesByName_WhenNoEmployees() {

    // Arrange
    when(employeeRepository.findByName("John"))
            .thenReturn(List.of());

    // Act
    List<EmployeeResponse> result =
            employeeService.findEmployeesByName("John");

    // Assert
    assertEquals(0, result.size());

    // Verify
    verify(employeeRepository).findByName("John");

    verify(employeeMapper, never())
            .toResponse(any(Employee.class));
}

@Test
void testFindEmployeesBySalary() {

    // Arrange
    Employee employee1 = new Employee();
    employee1.setId(1);
    employee1.setName("John");
    employee1.setSalary(50000.0);

    Employee employee2 = new Employee();
    employee2.setId(2);
    employee2.setName("David");
    employee2.setSalary(50000.0);

    EmployeeResponse response1 = new EmployeeResponse();
    response1.setId(1);
    response1.setName("John");
    response1.setSalary(50000.0);

    EmployeeResponse response2 = new EmployeeResponse();
    response2.setId(2);
    response2.setName("David");
    response2.setSalary(50000.0);

    when(employeeRepository.findBySalary(50000.0))
            .thenReturn(List.of(employee1, employee2));

    when(employeeMapper.toResponse(employee1))
            .thenReturn(response1);

    when(employeeMapper.toResponse(employee2))
            .thenReturn(response2);

    // Act
    List<EmployeeResponse> result =
            employeeService.findEmployeesBySalary(50000.0);

    // Assert
    assertEquals(2, result.size());

    assertEquals("John", result.get(0).getName());
    assertEquals(50000.0, result.get(0).getSalary());

    assertEquals("David", result.get(1).getName());
    assertEquals(50000.0, result.get(1).getSalary());

    // Verify
    verify(employeeRepository).findBySalary(50000.0);

    verify(employeeMapper).toResponse(employee1);
    verify(employeeMapper).toResponse(employee2);
}
@Test
void testFindEmployeesBySalary_WhenNoEmployees() {

    // Arrange
    when(employeeRepository.findBySalary(50000.0))
            .thenReturn(List.of());

    // Act
    List<EmployeeResponse> result =
            employeeService.findEmployeesBySalary(50000.0);

    // Assert
    assertEquals(0, result.size());

    // Verify
    verify(employeeRepository).findBySalary(50000.0);

    verify(employeeMapper, never())
            .toResponse(any(Employee.class));
}
}

