package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.ReportStatistics;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ReportService {

    ReportStatistics<LocalDate, BigDecimal> readSalaryExpenses(LocalDate from, LocalDate to);

    ReportStatistics<LocalDate, BigDecimal> readOrderIncomes(LocalDate from, LocalDate to);

    ReportStatistics<LocalDate, BigDecimal> readOrderCosts(LocalDate from, LocalDate to);

}
