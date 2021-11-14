package com.ktsnvt.ktsnvt.task;

import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableAsync
@Component
public class SendMonthlyFinancialReport {

    private final ReportService reportService;
    private final LocalDateTimeService localDateTimeService;

    @Autowired
    public SendMonthlyFinancialReport(ReportService reportService, LocalDateTimeService localDateTimeService) {
        this.reportService = reportService;
        this.localDateTimeService = localDateTimeService;
    }

    @Async
    @Scheduled(cron = "0 * * * * *")
    public void sendReport() {
        reportService.generateMonthlyFinancialReport(localDateTimeService.currentDate().minusMonths(1),
                localDateTimeService.currentDate());
    }
}
