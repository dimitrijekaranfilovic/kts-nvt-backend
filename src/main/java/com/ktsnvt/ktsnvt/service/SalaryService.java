package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.Salary;
import com.ktsnvt.ktsnvt.model.User;

public interface SalaryService {
    void endActiveSalaryForUser(User user);

    void updateUserSalary(Integer id, Salary salary);
}
