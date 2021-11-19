package com.ktsnvt.ktsnvt.controller;

import com.ktsnvt.ktsnvt.annotations.IsAdmin;
import com.ktsnvt.ktsnvt.dto.readreports.ReadReportsRequest;
import com.ktsnvt.ktsnvt.dto.readreports.ReadReportsResponse;
import com.ktsnvt.ktsnvt.model.ReportStatistics;
import com.ktsnvt.ktsnvt.service.ReportService;
import com.ktsnvt.ktsnvt.support.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("api/reports")
public class ReportsController {
    private final ReportService reportService;

    private final EntityConverter<ReportStatistics<LocalDate, BigDecimal>, ReadReportsResponse> toReportResponse;

    @Autowired
    public ReportsController(ReportService reportService,
                             EntityConverter<ReportStatistics<LocalDate, BigDecimal>, ReadReportsResponse> toReportResponse) {
        this.reportService = reportService;
        this.toReportResponse = toReportResponse;
    }

    @IsAdmin
    @GetMapping("/salary-costs")
    @ResponseStatus(HttpStatus.OK)
    public ReadReportsResponse readSalaryExpenses(ReadReportsRequest request) {
        var result = reportService.readSalaryExpenses(request.getFromLocalDate(), request.getToLocalDate());
        return toReportResponse.convert(result);
    }

    @IsAdmin
    @GetMapping("/order-costs")
    @ResponseStatus(HttpStatus.OK)
    public ReadReportsResponse readOrderCosts(ReadReportsRequest request) {
        var result = reportService.readOrderCosts(request.getFromLocalDate(), request.getToLocalDate());
        return toReportResponse.convert(result);
    }

    @IsAdmin
    @GetMapping("/order-incomes")
    @ResponseStatus(HttpStatus.OK)
    public ReadReportsResponse readOrderIncomes(ReadReportsRequest request) {
        var result = reportService.readOrderIncomes(request.getFromLocalDate(), request.getToLocalDate());
        return toReportResponse.convert(result);
    }
}
