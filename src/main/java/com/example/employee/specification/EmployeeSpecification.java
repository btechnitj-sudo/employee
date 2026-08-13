package com.example.employee.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.employee.model.Employee;

public class EmployeeSpecification {

    public static Specification<Employee> hasName(String name) {

        return (root, query, cb) -> {

            if (name == null || name.isBlank()) {
                return null;
            }

            return cb.equal(root.get("name"), name);
        };
    }

    public static Specification<Employee> hasSalaryGreaterThan(Double salary) {

        return (root, query, cb) -> {

            if (salary == null) {
                return null;
            }

            return cb.greaterThan(root.get("salary"), salary);
        };
    }

    public static Specification<Employee> hasDepartment(String department) {

        return (root, query, cb) -> {

            if (department == null || department.isBlank()) {
                return null;
            }

            return cb.equal(
                    root.get("department").get("name"),
                    department
            );
        };
    }

}
