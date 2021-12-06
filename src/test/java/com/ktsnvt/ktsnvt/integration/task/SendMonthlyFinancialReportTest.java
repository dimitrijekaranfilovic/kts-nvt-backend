package com.ktsnvt.ktsnvt.integration.task;

import com.ktsnvt.ktsnvt.task.SendMonthlyFinancialReport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Transactional
class SendMonthlyFinancialReportTest {

    @Autowired
    private SendMonthlyFinancialReport sendMonthlyFinancialReport;


    @Test
    void sendReport_calledImmediately_isSuccess() {
        assertDoesNotThrow(() -> sendMonthlyFinancialReport.sendReport());
    }
}
