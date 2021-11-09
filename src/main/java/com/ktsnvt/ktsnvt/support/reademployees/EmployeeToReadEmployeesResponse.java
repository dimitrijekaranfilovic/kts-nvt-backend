package com.ktsnvt.ktsnvt.support.reademployees;

import com.ktsnvt.ktsnvt.dto.reademployees.ReadEmployeesResponse;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class EmployeeToReadEmployeesResponse extends AbstractConverter<Employee, ReadEmployeesResponse> {
    @Override
    public ReadEmployeesResponse convert(@NonNull Employee source) {
        return getModelMapper().map(source, ReadEmployeesResponse.class);
    }
}
