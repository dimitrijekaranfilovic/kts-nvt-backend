package com.ktsnvt.ktsnvt.dto.reademployees;

import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReadEmployeesResponse {

    private Integer id;

    private String name;

    private String surname;

    private String pin;

    private EmployeeType type;

    private BigDecimal currentSalary;

}
