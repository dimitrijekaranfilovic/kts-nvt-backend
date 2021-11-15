package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.Salary;
import com.ktsnvt.ktsnvt.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SalaryService {
    void endActiveSalaryForUser(User user);

    void updateUserSalary(Integer id, Salary salary);

    BigDecimal readExpensesForDate(LocalDate date);
}
