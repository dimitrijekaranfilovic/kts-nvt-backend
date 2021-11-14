package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.model.Order;
import com.ktsnvt.ktsnvt.model.ReportStatistics;
import com.ktsnvt.ktsnvt.repository.OrderRepository;
import com.ktsnvt.ktsnvt.repository.SalaryRepository;
import com.ktsnvt.ktsnvt.service.EmailService;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.service.ReportService;
import com.ktsnvt.ktsnvt.service.SuperUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;

@Service
public class ReportServiceImpl implements ReportService {
    private final SalaryRepository salaryRepository;
    private final OrderRepository orderRepository;
    private final EmailService emailService;
    private final SuperUserService superUserService;

    private final LocalDateTimeService localDateTimeService;

    @Autowired
    public ReportServiceImpl(SalaryRepository salaryRepository, OrderRepository orderRepository,
                             EmailService emailService, SuperUserService superUserService,
                             LocalDateTimeService localDateTimeService) {
        this.salaryRepository = salaryRepository;
        this.orderRepository = orderRepository;
        this.emailService = emailService;
        this.superUserService = superUserService;
        this.localDateTimeService = localDateTimeService;
    }

    protected interface StatisticsCollector<T> {
        T collect(LocalDate date);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ReportStatistics<LocalDate, BigDecimal> readSalaryExpenses(LocalDate from, LocalDate to) {
        return readReportTemplate(from, to, date -> {
            BigDecimal amount = salaryRepository.readExpensesForDate(date);
            if (amount == null) {
                return BigDecimal.ZERO;
            }
            return amount.divide(BigDecimal.valueOf(date.lengthOfMonth()), RoundingMode.CEILING);
        });
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ReportStatistics<LocalDate, BigDecimal> readOrderIncomes(LocalDate from, LocalDate to) {
        var lookup = new HashMap<LocalDate, BigDecimal>();
        orderRepository
                .streamChargedOrdersInTimeRange(from.atStartOfDay(), to.plusDays(1).atStartOfDay())
                .forEach(order -> {
                    var income = lookup.getOrDefault(order.getServedAt().toLocalDate(), BigDecimal.ZERO);
                    lookup.put(order.getServedAt().toLocalDate(), income.add(order.getTotalIncome()));
                });
        return readReportTemplate(from, to, date -> lookup.getOrDefault(date, BigDecimal.ZERO));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ReportStatistics<LocalDate, BigDecimal> readOrderCosts(LocalDate from, LocalDate to) {
        var lookup = new HashMap<LocalDate, BigDecimal>();
        orderRepository
                .streamChargedOrdersInTimeRange(from.atStartOfDay(), to.plusDays(1).atStartOfDay())
                .forEach(order -> {
                    var cost = lookup.getOrDefault(order.getServedAt().toLocalDate(), BigDecimal.ZERO);
                    lookup.put(order.getServedAt().toLocalDate(), cost.add(order.getTotalCost()));
                });
        return readReportTemplate(from, to, date -> lookup.getOrDefault(date, BigDecimal.ZERO));
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BigDecimal readTotalSalaryExpense(LocalDate from, LocalDate to) {
        return this.readSalaryExpenses(from, to).getValues()
                .parallelStream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BigDecimal readTotalOrderIncome(LocalDate from, LocalDate to) {
        return orderRepository.streamChargedOrdersInTimeRange(from.atStartOfDay(), to.plusDays(1).atStartOfDay())
                .parallel()
                .map(Order::getTotalIncome)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BigDecimal readTotalOrderCost(LocalDate from, LocalDate to) {
        return orderRepository.streamChargedOrdersInTimeRange(from.atStartOfDay(), to.plusDays(1).atStartOfDay())
                .parallel()
                .map(Order::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Async
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void generateMonthlyFinancialReport(LocalDate from, LocalDate to) {
        superUserService.readAll()
                .forEach(superUser -> emailService.sendMonthlyFinancialReport(superUser,
                        readTotalSalaryExpense(from, to),
                        readTotalOrderIncome(from, to),
                        readTotalOrderCost(from, to)));

    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
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
