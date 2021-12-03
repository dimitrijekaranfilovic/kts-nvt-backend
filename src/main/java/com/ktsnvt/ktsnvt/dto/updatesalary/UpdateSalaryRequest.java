package com.ktsnvt.ktsnvt.dto.updatesalary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class UpdateSalaryRequest {

    @NotNull(message = "Salary must be provided.")
    @PositiveOrZero(message = "Salary must not be negative.")
    private BigDecimal amount;

}
