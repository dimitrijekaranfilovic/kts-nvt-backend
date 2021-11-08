package com.ktsnvt.ktsnvt.dto.createemployee;

import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class CreateEmployeeRequest {

    @NotBlank(message = "Pin cannot be blank.")
    private String pin;

    @NotBlank(message = "First name cannot be blank.")
    private String name;

    @NotBlank(message = "Last name cannot be blank.")
    private String surname;

    @Positive(message = "Salary must be greater than zero.")
    private double salary;

    @NotNull(message = "Employee type is required.")
    private EmployeeType type;

}
