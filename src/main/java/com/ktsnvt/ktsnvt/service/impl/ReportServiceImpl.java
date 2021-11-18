package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.model.Order;
import com.ktsnvt.ktsnvt.model.ReportStatistics;
import com.ktsnvt.ktsnvt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;

@Service
public class ReportServiceImpl extends TransactionalServiceBase implements ReportService {
    private final EmailService emailService;
    private final SalaryService salaryService;
    private final OrderService orderService;
    private final SuperUserService superUserService;
    private final LocalDateTimeService localDateTimeService;

    @Autowired
    public ReportServiceImpl(EmailService emailService,
                             SalaryService salaryService,
                             OrderService orderService,
                             SuperUserService superUserService,
                             LocalDateTimeService localDateTimeService) {
        this.salaryService = salaryService;
        this.orderService = orderService;
        this.emailService = emailService;
        this.superUserService = superUserService;
        this.localDateTimeService = localDateTimeService;
    }

    protected interface StatisticsCollector<T> {
        T collect(LocalDate date);
    }

    @Override
    public ReportStatistics<LocalDate, BigDecimal> readSalaryExpenses(LocalDate from, LocalDate to) {
        return readReportTemplate(from, to, date -> {
            BigDecimal amount = salaryService.readExpensesForDate(date);
            return amount.divide(BigDecimal.valueOf(date.lengthOfMonth()), RoundingMode.CEILING);
        });
    }

    @Override
    public ReportStatistics<LocalDate, BigDecimal> readOrderIncomes(LocalDate from, LocalDate to) {
        var lookup = new HashMap<LocalDate, BigDecimal>();
        orderService
                .streamChargedOrdersInTimeRange(from, to)
                .forEach(order -> {
                    var income = lookup.getOrDefault(order.getServedAt().toLocalDate(), BigDecimal.ZERO);
                    lookup.put(order.getServedAt().toLocalDate(), income.add(order.getTotalIncome()));
                });
        return readReportTemplate(from, to, date -> lookup.getOrDefault(date, BigDecimal.ZERO));
    }

    @Override
    public ReportStatistics<LocalDate, BigDecimal> readOrderCosts(LocalDate from, LocalDate to) {
        var lookup = new HashMap<LocalDate, BigDecimal>();
        orderService
                .streamChargedOrdersInTimeRange(from, to)
                .forEach(order -> {
                    var cost = lookup.getOrDefault(order.getServedAt().toLocalDate(), BigDecimal.ZERO);
                    lookup.put(order.getServedAt().toLocalDate(), cost.add(order.getTotalCost()));
                });
        return readReportTemplate(from, to, date -> lookup.getOrDefault(date, BigDecimal.ZERO));
    }

    @Override
    public BigDecimal readTotalSalaryExpense(LocalDate from, LocalDate to) {
        return this.readSalaryExpenses(from, to).getValues()
                .parallelStream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal readTotalOrderIncome(LocalDate from, LocalDate to) {
        return orderService.streamChargedOrdersInTimeRange(from, to)
                .parallel()
                .map(Order::getTotalIncome)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal readTotalOrderCost(LocalDate from, LocalDate to) {
        return orderService.streamChargedOrdersInTimeRange(from, to)
                .parallel()
                .map(Order::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Async
    public void generateMonthlyFinancialReport(LocalDate from, LocalDate to) {
        superUserService.readAll()
                .forEach(superUser -> emailService.sendMonthlyFinancialReport(superUser,
                        readTotalSalaryExpense(from, to),
                        readTotalOrderIncome(from, to),
                        readTotalOrderCost(from, to)));

    }

    public <T> ReportStatistics<LocalDate, T> readReportTemplate(LocalDate from, LocalDate to, StatisticsCollector<T> collector) {
        from = from == null ? localDateTimeService.currentDate().minusDays(30) : from;
        to = to == null ? localDateTimeService.currentDate() : to;
        ReportStatistics<LocalDate, T> statistics = new ReportStatistics<>();
        while (from.isBefore(to) || from.isEqual(to)) {
            statistics.addSample(from, collector.collect(from));
            from = from.plusDays(1);
        }
        return statistics;
    }
}
