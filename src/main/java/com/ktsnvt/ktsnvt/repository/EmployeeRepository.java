package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
