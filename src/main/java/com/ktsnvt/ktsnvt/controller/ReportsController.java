package com.ktsnvt.ktsnvt.controller;

import com.ktsnvt.ktsnvt.annotations.IsAdmin;
import com.ktsnvt.ktsnvt.annotations.IsSuperUser;
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

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;

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

    @IsSuperUser
    @GetMapping("/salary-costs")
    @ResponseStatus(HttpStatus.OK)
    public ReadReportsResponse readSalaryExpenses(@Valid ReadReportsRequest request) {
        var result = reportService.readSalaryExpenses(request.getFromLocalDate(), request.getToLocalDate());
        return toReportResponse.convert(result);
    }

    @IsSuperUser
    @GetMapping("/order-costs")
    @ResponseStatus(HttpStatus.OK)
    public ReadReportsResponse readOrderCosts(@Valid ReadReportsRequest request) {
        var result = reportService.readOrderCosts(request.getFromLocalDate(), request.getToLocalDate());
        return toReportResponse.convert(result);
    }

    @IsSuperUser
    @GetMapping("/order-incomes")
    @ResponseStatus(HttpStatus.OK)
    public ReadReportsResponse readOrderIncomes(@Valid ReadReportsRequest request) {
        var result = reportService.readOrderIncomes(request.getFromLocalDate(), request.getToLocalDate());
        return toReportResponse.convert(result);
    }
}
