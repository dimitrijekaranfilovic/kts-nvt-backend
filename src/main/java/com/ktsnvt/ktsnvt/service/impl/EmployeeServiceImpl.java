package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.repository.EmployeeRepository;
import com.ktsnvt.ktsnvt.service.AuthorityService;
import com.ktsnvt.ktsnvt.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Employee create(Employee employee) {
        var authority = authorityService.findByName(employee.getType().toString());
        employee.setAuthority(authority);
        return employeeRepository.save(employee);
    }
}
