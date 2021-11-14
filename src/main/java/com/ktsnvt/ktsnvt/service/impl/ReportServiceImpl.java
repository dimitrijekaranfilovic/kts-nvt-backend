package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.model.ReportStatistics;
import com.ktsnvt.ktsnvt.repository.OrderRepository;
import com.ktsnvt.ktsnvt.repository.SalaryRepository;
import com.ktsnvt.ktsnvt.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public ReportServiceImpl(SalaryRepository salaryRepository, OrderRepository orderRepository) {
        this.salaryRepository = salaryRepository;
        this.orderRepository = orderRepository;
    }

    protected interface StatisticsCollector<T> {
        T collect(LocalDate date);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ReportStatistics<LocalDate, BigDecimal> readSalaryExpenses(LocalDate from, LocalDate to) {
        return readReportTemplate(from, to, date -> {
            BigDecimal amount = salaryRepository.readExpensesForDate(date);
            return amount.divide(BigDecimal.valueOf(date.lengthOfMonth()), RoundingMode.CEILING);
        });
    }

    @Override
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

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public <T> ReportStatistics<LocalDate, T> readReportTemplate(LocalDate from, LocalDate to, StatisticsCollector<T> collector) {
        ReportStatistics<LocalDate, T> statistics = new ReportStatistics<>();
        while (from.isBefore(to)) {
            statistics.addSample(from, collector.collect(from));
            from = from.plusDays(1);
        }
        return statistics;
    }
}
