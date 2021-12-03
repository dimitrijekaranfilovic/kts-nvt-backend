package com.ktsnvt.ktsnvt.dto.createemployee;

import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class CreateEmployeeRequest {

    @NotBlank(message = "Pin cannot be blank.")
    private String pin;

    @NotBlank(message = "First name cannot be blank.")
    private String name;

    @NotBlank(message = "Last name cannot be blank.")
    private String surname;

    @NotNull(message = "Salary must be provided.")
    @PositiveOrZero(message = "Salary must not be negative.")
    private BigDecimal salary;

    @NotNull(message = "Employee type is required.")
    private EmployeeType type;

}
