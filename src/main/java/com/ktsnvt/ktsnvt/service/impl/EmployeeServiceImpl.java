package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.BusyEmployeeDeletionException;
import com.ktsnvt.ktsnvt.exception.EmployeeNotFoundException;
import com.ktsnvt.ktsnvt.exception.IllegalEmployeeTypeChangeException;
import com.ktsnvt.ktsnvt.exception.PinAlreadyExistsException;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.repository.EmployeeRepository;
import com.ktsnvt.ktsnvt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EmployeeServiceImpl extends TransactionalServiceBase implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    private final EmployeeQueryService employeeQueryService;
    private final AuthorityService authorityService;
    private final SalaryService salaryService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               EmployeeQueryService employeeQueryService,
                               AuthorityService authorityService,
                               SalaryService salaryService,
                               OrderService orderService,
                               OrderItemService orderItemService) {
        this.employeeRepository = employeeRepository;
        this.employeeQueryService = employeeQueryService;
        this.authorityService = authorityService;
        this.salaryService = salaryService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @Override
    public Employee create(Employee employee) {
        var samePinEmployee = employeeQueryService.findByPinUnchecked(employee.getPin());
        if (samePinEmployee.isPresent()) {
            throw new PinAlreadyExistsException(employee.getPin());
        }
        var authority = authorityService.findByName(employee.getType().toString());
        employee.setAuthority(authority);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee readForUpdate(Integer id) {
        return employeeRepository
                .findOneForUpdate(id)
                .orElseThrow(() -> new EmployeeNotFoundException(String.format("Employee with id: %d not found.", id)));
    }

    @Override
    public Employee read(Integer id) {
        return employeeRepository
                .findOneById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(String.format("Employee with id: %d not found.", id)));
    }

    @Override
    public Page<Employee> read(String query, BigDecimal salaryFrom, BigDecimal salaryTo, EmployeeType type, Pageable pageable) {
        return employeeRepository.findAll(query.trim().toLowerCase(), salaryFrom, salaryTo, type, pageable);
    }

    @Override
    public void delete(Integer id) {
        var employee = readForUpdate(id);
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

    @Override
    public void update(Integer id, String name, String surname, String pin, EmployeeType type) {
        var employee = readForUpdate(id);
        var samePinEmployee = employeeQueryService.findByPinUnchecked(pin);
        samePinEmployee.ifPresent(same -> {
            if (!same.getId().equals(employee.getId())) {
                throw new PinAlreadyExistsException(pin);
            }
        });
        // Basically if we want to change role of an employee, then we have to make sure that he has completed
        // all of his tasks in a previous role
        if (!employee.getType().equals(type)) {
            if (employee.getType().equals(EmployeeType.WAITER) && orderService.hasAssignedActiveOrders(employee)) {
                throw new IllegalEmployeeTypeChangeException("Employee has order which it has to process.");
            } else if (!employee.getType().equals(EmployeeType.WAITER) && orderItemService.hasActiveOrderItems(employee)) {
                throw new IllegalEmployeeTypeChangeException("Employee has order items which it has to prepare.");
            }
        }
        employee.setName(name);
        employee.setSurname(surname);
        employee.setPin(pin);
        employee.setType(type);
        var authority = authorityService.findByName(employee.getType().toString());
        employee.setAuthority(authority);
    }
}
