package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
@Transactional
class ReportServiceTest {
    @Autowired
    private ReportServiceImpl reportService;

    @Test
    void readSalaryExpenses_whenCalledWithValidDate_isSuccess() {
        var from = LocalDate.of(2021, 11, 30);
        var to = LocalDate.of(2021, 12, 4);
        var statistics = reportService.readSalaryExpenses(from, to);
        assertEquals(0, statistics.getValues().get(0).compareTo(BigDecimal.valueOf(50.40)));
        assertEquals(0, statistics.getValues().get(4).compareTo(BigDecimal.valueOf(82.65)));
    }

    @Test
    void readOrderIncomes_whenCalledWithValidDate_isSuccess() {
        var from = LocalDate.of(2020, 11, 30);
        var to = LocalDate.of(2021, 11, 14);
        var statistics = reportService.readOrderIncomes(from, to);
        assertEquals(0, statistics.getValues().get(0).compareTo(BigDecimal.valueOf(0)));
        assertEquals(0, statistics.getValues().get(statistics.getLabels().size() - 1).compareTo(BigDecimal.valueOf(3462)));
    }
}
