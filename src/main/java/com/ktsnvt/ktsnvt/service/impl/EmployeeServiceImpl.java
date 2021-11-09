package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.PinAlreadyExistsException;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.repository.EmployeeRepository;
import com.ktsnvt.ktsnvt.service.AuthorityService;
import com.ktsnvt.ktsnvt.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AuthorityService authorityService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, AuthorityService authorityService) {
        this.employeeRepository = employeeRepository;
        this.authorityService = authorityService;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Employee create(Employee employee) {
        var samePinEmployee = employeeRepository.findByPin(employee.getPin());
        if (samePinEmployee.isPresent()) {
            throw new PinAlreadyExistsException(employee.getPin());
        }
        var authority = authorityService.findByName(employee.getType().toString());
        employee.setAuthority(authority);
        return employeeRepository.save(employee);
    }

    @Override
    public Page<Employee> read(String query, BigDecimal salaryFrom, BigDecimal salaryTo, EmployeeType type, Pageable pageable) {
        return employeeRepository.findAll(query.trim().toLowerCase(), salaryFrom, salaryTo, type, pageable);
    }
}
