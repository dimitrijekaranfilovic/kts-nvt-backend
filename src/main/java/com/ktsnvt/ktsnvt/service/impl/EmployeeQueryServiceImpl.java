package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.EmployeeNotFoundException;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.repository.EmployeeRepository;
import com.ktsnvt.ktsnvt.service.EmployeeQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeQueryServiceImpl implements EmployeeQueryService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeQueryServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee findByPin(String pin) {
        return employeeRepository
                .findByPin(pin)
                .orElseThrow(() -> new EmployeeNotFoundException(String.format("Employee with pin: '%s' not found.", pin)));
    }

    @Override
    public Employee findByPinForUpdate(String pin, EmployeeType type) {
        return employeeRepository
                .getEmployeeByPinForUpdate(pin, type)
                .orElseThrow(() -> new EmployeeNotFoundException(String.format("Cannot find employee with pin: '%s' of type %s.", pin, type)));
    }
}
