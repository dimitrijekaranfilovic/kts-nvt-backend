package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.ReportStatistics;
import org.springframework.scheduling.annotation.Async;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ReportService {

    ReportStatistics<LocalDate, BigDecimal> readSalaryExpenses(LocalDate from, LocalDate to);

    ReportStatistics<LocalDate, BigDecimal> readOrderIncomes(LocalDate from, LocalDate to);

    ReportStatistics<LocalDate, BigDecimal> readOrderCosts(LocalDate from, LocalDate to);

    BigDecimal readTotalSalaryExpense(LocalDate from, LocalDate to);

    BigDecimal readTotalOrderIncome(LocalDate from, LocalDate to);

    BigDecimal readTotalOrderCost(LocalDate from, LocalDate to);

    @Async
    void generateMonthlyFinancialReport(LocalDate from, LocalDate to);

}
