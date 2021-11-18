package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.model.Salary;
import com.ktsnvt.ktsnvt.model.User;
import com.ktsnvt.ktsnvt.repository.SalaryRepository;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.service.SalaryService;
import com.ktsnvt.ktsnvt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class SalaryServiceImpl extends TransactionalServiceBase implements SalaryService {
    private final SalaryRepository salaryRepository;

    private final UserService userService;
    private final LocalDateTimeService localDateTimeService;

    @Autowired
    public SalaryServiceImpl(SalaryRepository salaryRepository, UserService userService, LocalDateTimeService localDateTimeService) {
        this.salaryRepository = salaryRepository;
        this.userService = userService;
        this.localDateTimeService = localDateTimeService;
    }

    @Override
    public void endActiveSalaryForUser(User user) {
        salaryRepository.findActiveForUser(user.getId())
                        .ifPresent(salary -> salary.setEndDate(localDateTimeService.currentDate()));
    }

    @Override
    public void updateUserSalary(Integer id, Salary salary) {
        var user = userService.readForSalaryUpdate(id);
        endActiveSalaryForUser(user);
        user.addSalary(salary);
    }

    @Override
    public BigDecimal readExpensesForDate(LocalDate date) {
        return salaryRepository.readExpensesForDate(date).orElse(BigDecimal.ZERO);
    }
}
