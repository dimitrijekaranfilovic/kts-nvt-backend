package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.service.*;
import com.ktsnvt.ktsnvt.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    public void setupLocalDateTimeService() {
        var currentDate = LocalDate.of(2021, 11, 30);
        doReturn(currentDate).when(localDateTimeService).currentDate();
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
}
