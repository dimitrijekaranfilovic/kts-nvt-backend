package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Optional<Employee> findEmployeeByPin(String pin);
}
