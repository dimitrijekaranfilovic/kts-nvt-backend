package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class LocalDateTimeServiceImpl implements LocalDateTimeService {
    @Override
    public LocalDateTime currentTime() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDate currentDate() {
        return LocalDate.now();
    }
}
