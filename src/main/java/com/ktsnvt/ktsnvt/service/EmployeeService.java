package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface EmployeeService {
    Employee create(Employee employee);

    Employee readForUpdate(Integer id);

    Employee read(Integer id);

    Page<Employee> read(String query, BigDecimal salaryFrom, BigDecimal salaryTo, EmployeeType type, Pageable pageable);

    void delete(Integer id);

    void update(Integer id, String name, String surname, String pin, EmployeeType type);
}
