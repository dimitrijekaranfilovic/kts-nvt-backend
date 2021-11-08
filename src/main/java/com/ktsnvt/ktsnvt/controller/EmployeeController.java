package com.ktsnvt.ktsnvt.controller;

import com.ktsnvt.ktsnvt.dto.createemployee.CreateEmployeeRequest;
import com.ktsnvt.ktsnvt.dto.createemployee.CreateEmployeeResponse;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.service.EmployeeService;
import com.ktsnvt.ktsnvt.support.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    private final EntityConverter<CreateEmployeeRequest, Employee> createEmployeeToEmployee;
    private final EntityConverter<Employee, CreateEmployeeResponse> employeeToCreateEmployee;

    @Autowired
    public EmployeeController(EmployeeService employeeService,
                              EntityConverter<CreateEmployeeRequest, Employee> createEmployeeToEmployee,
                              EntityConverter<Employee, CreateEmployeeResponse> employeeToCreateEmployee) {
        this.employeeService = employeeService;
        this.createEmployeeToEmployee = createEmployeeToEmployee;
        this.employeeToCreateEmployee = employeeToCreateEmployee;
    }

    // PRE AUTHORIZER (ADMIN, MANAGER)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateEmployeeResponse createEmployee(@RequestBody CreateEmployeeRequest request) {
        var employee = createEmployeeToEmployee.convert(request);
        var result = employeeService.create(employee);
        return employeeToCreateEmployee.convert(result);
    }
}
