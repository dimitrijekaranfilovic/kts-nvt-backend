package com.ktsnvt.ktsnvt.model.projections;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class IncomeGroup {

    private LocalDate date;

    private BigDecimal amount;

}
