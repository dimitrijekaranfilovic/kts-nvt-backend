package com.ktsnvt.ktsnvt.dto.updateemployee;

import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateEmployeeRequest {

    @NotBlank(message = "First name cannot be blank.")
    private String name;

    @NotBlank(message = "Last name cannot be blank.")
    private String surname;

    @NotBlank(message = "PIN cannot be blank.")
    private String pin;

    @NotNull(message = "Employee type must be provided.")
    private EmployeeType type;

}
