package com.ktsnvt.ktsnvt.support.createemployee;

import com.ktsnvt.ktsnvt.dto.createemployee.CreateEmployeeRequest;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.Salary;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateEmployeeRequestToEmployee extends AbstractConverter<CreateEmployeeRequest, Employee> {
    private final LocalDateTimeService localDateTimeService;

    @Autowired
    public CreateEmployeeRequestToEmployee(LocalDateTimeService localDateTimeService) {
        this.localDateTimeService = localDateTimeService;
    }

    @Override
    public Employee convert(@NonNull CreateEmployeeRequest request) {
        var employee = getModelMapper().map(request, Employee.class);
        employee.addSalary(new Salary(
                localDateTimeService.currentDate(), null, request.getSalary(), employee
        ));
        return employee;
    }
}
