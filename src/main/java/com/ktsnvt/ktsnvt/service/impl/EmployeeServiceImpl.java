package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.BusyEmployeeDeletionException;
import com.ktsnvt.ktsnvt.exception.EmployeeNotFoundException;
import com.ktsnvt.ktsnvt.exception.PinAlreadyExistsException;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.repository.EmployeeRepository;
import com.ktsnvt.ktsnvt.service.*;
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
    private final SalaryService salaryService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, AuthorityService authorityService, SalaryService salaryService, OrderService orderService, OrderItemService orderItemService) {
        this.employeeRepository = employeeRepository;
        this.authorityService = authorityService;
        this.salaryService = salaryService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
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

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(Integer id) {
        var employee = employeeRepository
                .findOneForUpdate(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with id: " + id + " not found"));
        // Check if the employee has some orders assigned
        if (orderService.hasAssignedActiveOrders(employee)) {
            throw new BusyEmployeeDeletionException("Employee with id: " + id + " has active orders which he has to process.");
        }
        // Check if the employee has some active order items which he has to process
        if (orderItemService.hasActiveOrderItems(employee)) {
            throw new BusyEmployeeDeletionException("Employee with id: " + id + " has active requests which he has to process.");
        }
        salaryService.endActiveSalaryForUser(employee);
        employee.setIsActive(false);
    }
}
