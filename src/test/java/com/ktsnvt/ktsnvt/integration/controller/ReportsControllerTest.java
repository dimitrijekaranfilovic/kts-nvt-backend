package com.ktsnvt.ktsnvt.integration.controller;

import com.ktsnvt.ktsnvt.model.ReportStatistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class ReportsControllerTest extends AuthorizingControllerRestTemplateTestBase {

    // Have to do this to avoid Java's type erasure problems...
    public static class LocalDateBigDecimalReportStatistics extends ReportStatistics<LocalDate, BigDecimal> {}

    @Autowired
    public ReportsControllerTest(TestRestTemplate restTemplate) {
        super(restTemplate);
    }

    @Test
    void readSalaryExpenses_whenCalledWithValidDate_isSuccess() {
        login("email2@email.com", "password");
        var from = LocalDate.of(2021, 11, 30);
        var to = LocalDate.of(2021, 12, 4);

        var url = String.format("/api/reports/salary-costs?from=%s&to=%s", from, to);
        var responseEntity = restTemplate.exchange(url, HttpMethod.GET, makeEntity(), LocalDateBigDecimalReportStatistics.class);

        assertNotNull(responseEntity.getBody());
        var statistics = Objects.requireNonNull(responseEntity.getBody());
        assertEquals(0, statistics.getValues().get(0).compareTo(BigDecimal.valueOf(50.40)));
        assertEquals(0, statistics.getValues().get(4).compareTo(BigDecimal.valueOf(82.65)));
    }
}
