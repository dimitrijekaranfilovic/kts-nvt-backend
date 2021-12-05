package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void readOrderCosts_whenCalledWithValidDate_isSuccess() {
        var from = LocalDate.of(2020, 11, 30);
        var to = LocalDate.of(2021, 11, 14);
        var statistics = reportService.readOrderCosts(from, to);
        assertEquals(0, statistics.getValues().get(0).compareTo(BigDecimal.valueOf(0)));
        assertEquals(0, statistics.getValues().get(statistics.getLabels().size() - 1).compareTo(BigDecimal.valueOf(162)));
    }

    @ParameterizedTest
    @MethodSource("provideValidDateRangesAndExpectedTotalsForSalaryExpenses")
    void readTotalSalaryExpense_whenCalledWithValidDate_isSuccess(LocalDate from, LocalDate to,
                                                                  BigDecimal expectedTotal) {
        var returnedTotal = reportService.readTotalSalaryExpense(from, to);
        assertEquals(0, expectedTotal.compareTo(returnedTotal));
    }

    @ParameterizedTest
    @MethodSource("provideValidDateRangesAndExpectedTotalForTotalOrderIncome")
    void readTotalOrderIncome_whenCalledWithValidDate_isSuccess(LocalDate from, LocalDate to,
                                                                BigDecimal expectedTotal) {
        var returnedTotal = reportService.readTotalOrderIncome(from, to);
        assertEquals(0, expectedTotal.compareTo(returnedTotal));
    }

    @ParameterizedTest
    @MethodSource("provideValidDateRangesAndExpectedTotalForTotalOrderCost")
    void readTotalOrderCost_whenCalledWithValidDate_isSuccess(LocalDate from, LocalDate to,
                                                              BigDecimal expectedTotal) {
        var returnedTotal = reportService.readTotalOrderCost(from, to);
        assertEquals(0, expectedTotal.compareTo(returnedTotal));
    }

    @ParameterizedTest
    @MethodSource("provideValidDateRangesForGeneratingMonthlyFinancialReport")
    void generateMonthlyFinancialReport_whenCalledWithValidDate_isSuccess(LocalDate from, LocalDate to) {
        assertDoesNotThrow(() -> reportService.generateMonthlyFinancialReport(from, to));
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideValidDateRangesAndExpectedTotalsForSalaryExpenses() {
        return Stream.of(
                Arguments.of(LocalDate.of(2021, 1, 1),
                        LocalDate.of(2021, 11, 30), BigDecimal.valueOf(16633.08)),
                Arguments.of(LocalDate.of(2021, 1, 1),
                        LocalDate.of(2021, 12, 1), BigDecimal.valueOf(16715.73)),
                Arguments.of(LocalDate.of(2021, 1, 1),
                        LocalDate.of(2021, 12, 30), BigDecimal.valueOf(19112.58)),
                Arguments.of(LocalDate.of(2019, 1, 1),
                        LocalDate.of(2020, 1, 1), BigDecimal.valueOf(0)),
                Arguments.of(LocalDate.of(2022, 1, 1),
                        LocalDate.of(2022, 3, 31), BigDecimal.valueOf(7686.30)),
                Arguments.of(LocalDate.of(2021, 12, 31),
                        LocalDate.of(2021, 12, 1), BigDecimal.valueOf(0))
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideValidDateRangesAndExpectedTotalForTotalOrderIncome() {
        return Stream.of(
                Arguments.of(LocalDate.of(2010, 1, 1),
                        LocalDate.of(2011, 11, 30), BigDecimal.valueOf(0)),
                Arguments.of(LocalDate.of(2021, 11, 10),
                        LocalDate.of(2021, 11, 15), BigDecimal.valueOf(3462)),
                Arguments.of(LocalDate.of(2021, 11, 10),
                        LocalDate.of(2021, 11, 13), BigDecimal.valueOf(0)),
                Arguments.of(LocalDate.of(2021, 11, 14),
                        LocalDate.of(2021, 11, 15), BigDecimal.valueOf(3462)),
                Arguments.of(LocalDate.of(2021, 12, 1),
                        LocalDate.of(2021, 12, 31), BigDecimal.valueOf(0)),
                Arguments.of(LocalDate.of(2021, 12, 31),
                        LocalDate.of(2021, 12, 1), BigDecimal.valueOf(0))
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideValidDateRangesAndExpectedTotalForTotalOrderCost() {
        return Stream.of(
                Arguments.of(LocalDate.of(2010, 1, 1),
                        LocalDate.of(2011, 11, 30), BigDecimal.valueOf(0)),
                Arguments.of(LocalDate.of(2021, 11, 10),
                        LocalDate.of(2021, 11, 15), BigDecimal.valueOf(162)),
                Arguments.of(LocalDate.of(2021, 11, 10),
                        LocalDate.of(2021, 11, 13), BigDecimal.valueOf(0)),
                Arguments.of(LocalDate.of(2021, 11, 14),
                        LocalDate.of(2021, 11, 15), BigDecimal.valueOf(162)),
                Arguments.of(LocalDate.of(2021, 12, 1),
                        LocalDate.of(2021, 12, 31), BigDecimal.valueOf(0)),
                Arguments.of(LocalDate.of(2021, 12, 31),
                        LocalDate.of(2021, 12, 1), BigDecimal.valueOf(0))
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideValidDateRangesForGeneratingMonthlyFinancialReport() {
        return Stream.of(
                Arguments.of(LocalDate.of(2010, 1, 1),
                        LocalDate.of(2011, 11, 30)),
                Arguments.of(LocalDate.of(2020, 1, 1),
                        LocalDate.of(2022, 1, 1)),
                Arguments.of(LocalDate.of(2022, 1, 1),
                        LocalDate.of(2020, 1, 1))
        );
    }
}
