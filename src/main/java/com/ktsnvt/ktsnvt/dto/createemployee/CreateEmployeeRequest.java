package com.ktsnvt.ktsnvt.dto.createemployee;

import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class CreateEmployeeRequest {

    @NotBlank(message = "Pin cannot be blank.")
    private String pin;

    @NotBlank(message = "First name cannot be blank.")
    private String name;

    @NotBlank(message = "Last name cannot be blank.")
    private String surname;

    @NotNull(message = "Salary must be provided.")
    @Positive(message = "Salary must be greater than zero.")
    private BigDecimal salary;

    @NotNull(message = "Employee type is required.")
    private EmployeeType type;

}
