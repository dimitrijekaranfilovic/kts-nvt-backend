package com.ktsnvt.ktsnvt.support.createemployee;

import com.ktsnvt.ktsnvt.dto.createemployee.CreateEmployeeResponse;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class EmployeeToCreateEmployeeResponse extends AbstractConverter<Employee, CreateEmployeeResponse> {
    @Override
    public CreateEmployeeResponse convert(@NonNull Employee employee) {
        return getModelMapper().map(employee, CreateEmployeeResponse.class);
    }
}
