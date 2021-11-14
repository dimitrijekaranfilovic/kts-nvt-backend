package com.ktsnvt.ktsnvt.task;

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

    @Autowired
    public SendMonthlyFinancialReport(ReportService reportService) {
        this.reportService = reportService;
    }

    @Async
    @Scheduled(cron = "0 * * * * *")
    public void SendMonthlyFinancialReport() {
        reportService.generateMonthlyFinancialReport();
    }
}
