package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.model.User;
import com.ktsnvt.ktsnvt.repository.SalaryRepository;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalaryServiceImpl implements SalaryService {
    private final SalaryRepository salaryRepository;
    private final LocalDateTimeService localDateTimeService;

    @Autowired
    public SalaryServiceImpl(SalaryRepository salaryRepository, LocalDateTimeService localDateTimeService) {
        this.salaryRepository = salaryRepository;
        this.localDateTimeService = localDateTimeService;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void endActiveSalaryForUser(User user) {
        salaryRepository.findActiveForUser(user.getId())
                        .ifPresent(salary -> salary.setEndDate(localDateTimeService.currentDate()));
    }
}
