package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {


    Optional<Employee> findEmployeeByPin(String pin);

    Optional<Employee> findByPin(String pin);

    @Query("select e from Employee e where ((lower(e.name) like concat('%', :query, '%')) or (lower(e.surname) like concat('%', :query, '%')))" +
            " and e.currentSalary >= :salaryFrom and e.currentSalary <= :salaryTo and (:type is null or e.type = : type)")
    Page<Employee> findAll(String query, BigDecimal salaryFrom, BigDecimal salaryTo, EmployeeType type, Pageable pageable);
}
