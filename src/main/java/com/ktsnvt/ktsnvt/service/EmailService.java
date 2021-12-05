package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.SuperUser;
import org.springframework.scheduling.annotation.Async;

import java.math.BigDecimal;

public interface EmailService {

    @Async
    void sendSimpleMessage(String to, String subject, String text);

    @Async
    void sendMonthlyFinancialReport(SuperUser user, BigDecimal totalSalaryExpense,
                                    BigDecimal totalOrderIncome, BigDecimal totalOrderCost);
}
