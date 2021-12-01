package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.model.Order;
import com.ktsnvt.ktsnvt.model.ReportStatistics;
import com.ktsnvt.ktsnvt.service.*;
import com.ktsnvt.ktsnvt.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class ReportServiceTest {
    @Mock
    private EmailService emailService;
    @Mock
    private SalaryService salaryService;
    @Mock
    private OrderService orderService;
    @Mock
    private SuperUserService superUserService;
    @Mock
    private LocalDateTimeService localDateTimeService;
    @InjectMocks
    private ReportServiceImpl reportService;

    private final LocalDate first = LocalDate.of(2021, 11, 28);
    private final LocalDate second = LocalDate.of(2021, 11, 29);
    private final LocalDate third = LocalDate.of(2021, 11, 30);

    @BeforeEach
    public void setupLocalDateTimeService() {
        var currentDate = LocalDate.of(2021, 11, 30);
        doReturn(currentDate).when(localDateTimeService).currentDate();
    }

    @Test
    void readSalaryExpenses_whenCalledWithValidDate_isSuccess() {
        // GIVEN
        doReturn(BigDecimal.valueOf(300L)).when(salaryService).readExpensesForDate(first);
        doReturn(BigDecimal.valueOf(600L)).when(salaryService).readExpensesForDate(second);
        doReturn(BigDecimal.valueOf(0L)).when(salaryService).readExpensesForDate(third);

        // WHEN
        var statistics = reportService.readSalaryExpenses(first, third);

        // THEN
        assertReportTestCase(statistics, BigDecimal.valueOf(10L), BigDecimal.valueOf(20L), BigDecimal.valueOf(0L));
    }

    @Test
    void readOrderIncomes_whenCalledWithValidDate_isSuccess() {
        // GIVEN
        doReturn(buildTestDataForStreamingOrders()).when(orderService).streamChargedOrdersInTimeRange(first, third);

        // WHEN
        var statistics = reportService.readOrderIncomes(first, third);

        // THEN
        assertReportTestCase(statistics, BigDecimal.valueOf(330L), BigDecimal.valueOf(400L), BigDecimal.ZERO);
    }

    @Test
    void readOrderCosts_whenCalledWithValidDate_isSuccess() {
        // GIVEN
        doReturn(buildTestDataForStreamingOrders()).when(orderService).streamChargedOrdersInTimeRange(first, third);

        // WHEN
        var statistics = reportService.readOrderCosts(first, third);

        // THEN
        assertReportTestCase(statistics, BigDecimal.valueOf(230L), BigDecimal.valueOf(240L), BigDecimal.ZERO);
    }

    @Test
    void readReportTemplate_whenCalledWithValidDate_isSuccess() {
        // GIVEN
        var fromDate = LocalDate.of(2021, 11, 27);
        var toDate = LocalDate.of(2021, 11, 30);

        // WHEN
        var statistics = reportService.readReportTemplate(fromDate, toDate, (date) -> 999);

        // THEN
        assertEquals(4, statistics.getValues().size());
        assertEquals(4, statistics.getLabels().size());
        assertEquals(fromDate, statistics.getLabels().get(0));
        assertEquals(toDate, statistics.getLabels().get(3));
        assertEquals(999, statistics.getValues().get(2));
    }

    @Test
    void readReportTemplate_whenCalledWithNullDates_isSuccess() {
        // GIVEN
        LocalDate startDate = LocalDate.of(2021, 10, 31);
        LocalDate endDate = LocalDate.of(2021, 11, 30);

        // WHEN
        var statistics = reportService.readReportTemplate(null, null, (date) -> 999);

        // THEN
        assertEquals(31, statistics.getValues().size());
        assertEquals(31, statistics.getLabels().size());
        assertEquals(startDate, statistics.getLabels().get(0));
        assertEquals(endDate, statistics.getLabels().get(30));
        assertEquals(999, statistics.getValues().get(2));
    }

    @ParameterizedTest
    @MethodSource("provideTestsForReadTemplate")
    void readReportTemplate_whenCalledWithValidDates_hasCorrectNumberOfElements(LocalDate from, LocalDate to, int expected) {
        // GIVEN

        // WHEN
        var statistics = reportService.readReportTemplate(from, to, (date) -> 999);

        // THEN
        assertEquals(expected, statistics.getLabels().size());
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideTestsForReadTemplate() {
        return Stream.of(
                Arguments.of(LocalDate.of(2021, 11, 14), LocalDate.of(2021, 11, 16), 3),
                Arguments.of(LocalDate.of(2021, 11, 30), LocalDate.of(2021, 11, 30), 1),
                Arguments.of(LocalDate.of(2021, 11, 30), LocalDate.of(2021, 11, 16), 0)
        );
    }

    private <T> void assertReportTestCase(ReportStatistics<LocalDate, T> statistics, T one, T two, T three) {
        assertEquals(3, statistics.getLabels().size());
        assertEquals(first, statistics.getLabels().get(0));
        assertEquals(second, statistics.getLabels().get(1));
        assertEquals(third, statistics.getLabels().get(2));
        assertEquals(one, statistics.getValues().get(0));
        assertEquals(two, statistics.getValues().get(1));
        assertEquals(three, statistics.getValues().get(2));
    }

    private Stream<Order> buildTestDataForStreamingOrders() {
        return Stream.of(
                makeOrder(first, 150L, 70L),
                makeOrder(second, 200L, 140L),
                makeOrder(first, 180L, 160L),
                makeOrder(second, 200L, 100L)
        );
    }

    private Order makeOrder(LocalDate servedAt, long income, long cost) {
        var order = new Order();
        order.setServedAt(servedAt.atStartOfDay());
        order.setTotalIncome(BigDecimal.valueOf(income));
        order.setTotalCost(BigDecimal.valueOf(cost));
        return order;
    }
}
