package com.ktsnvt.ktsnvt.dto.reademployees;

import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReadEmployeesRequest {

    private String query = "";

    private BigDecimal salaryLowerBound = BigDecimal.valueOf(0);

    private BigDecimal salaryUpperBound = BigDecimal.valueOf(Long.MAX_VALUE);

    private EmployeeType type;
}
