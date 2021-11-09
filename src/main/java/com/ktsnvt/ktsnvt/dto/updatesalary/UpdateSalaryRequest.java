package com.ktsnvt.ktsnvt.dto.updatesalary;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
public class UpdateSalaryRequest {

    @NotNull(message = "Salary must be provided.")
    @PositiveOrZero(message = "Salary must not be negative.")
    private BigDecimal amount;

}
