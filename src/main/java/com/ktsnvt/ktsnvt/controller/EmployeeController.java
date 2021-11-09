package com.ktsnvt.ktsnvt.controller;

import com.ktsnvt.ktsnvt.dto.createemployee.CreateEmployeeRequest;
import com.ktsnvt.ktsnvt.dto.createemployee.CreateEmployeeResponse;
import com.ktsnvt.ktsnvt.dto.reademployees.ReadEmployeesRequest;
import com.ktsnvt.ktsnvt.dto.reademployees.ReadEmployeesResponse;
import com.ktsnvt.ktsnvt.dto.updateemployee.UpdateEmployeeRequest;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.service.EmployeeService;
import com.ktsnvt.ktsnvt.support.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    private final EntityConverter<CreateEmployeeRequest, Employee> createEmployeeToEmployee;
    private final EntityConverter<Employee, CreateEmployeeResponse> employeeToCreateEmployee;

    private final EntityConverter<Employee, ReadEmployeesResponse> employeeToReadEmployee;

    @Autowired
    public EmployeeController(EmployeeService employeeService,
                              EntityConverter<CreateEmployeeRequest, Employee> createEmployeeToEmployee,
                              EntityConverter<Employee, CreateEmployeeResponse> employeeToCreateEmployee, EntityConverter<Employee, ReadEmployeesResponse> employeeToReadEmployee) {
        this.employeeService = employeeService;
        this.createEmployeeToEmployee = createEmployeeToEmployee;
        this.employeeToCreateEmployee = employeeToCreateEmployee;
        this.employeeToReadEmployee = employeeToReadEmployee;
    }

    // PRE AUTHORIZE (ADMIN, MANAGER)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateEmployeeResponse createEmployee(@RequestBody @Valid CreateEmployeeRequest request) {
        var employee = createEmployeeToEmployee.convert(request);
        var result = employeeService.create(employee);
        return employeeToCreateEmployee.convert(result);
    }

    // PRE AUTHORIZE (ADMIN, MANAGER)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ReadEmployeesResponse> readEmployees(ReadEmployeesRequest request, @PageableDefault Pageable pageable) {
        var page = employeeService.read(request.getQuery(), request.getSalaryLowerBound(), request.getSalaryUpperBound(), request.getType(), pageable);
        return page.map(employeeToReadEmployee::convert);
    }

    // PRE AUTHORIZE (ADMIN, MANAGER)
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody @Valid UpdateEmployeeRequest request) {
        employeeService.update(id, request.getName(), request.getSurname(), request.getPin(), request.getType());
    }

    // PRE AUTHORIZE (ADMIN, MANAGER)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Integer id) {
        employeeService.delete(id);
    }
}
