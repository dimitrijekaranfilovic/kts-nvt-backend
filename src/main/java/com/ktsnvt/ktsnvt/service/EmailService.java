package com.ktsnvt.ktsnvt.service;

import org.springframework.scheduling.annotation.Async;

public interface EmailService {

    @Async
    void sendMonthlyFinanceReport();
}
