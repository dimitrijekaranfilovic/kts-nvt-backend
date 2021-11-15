package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;

public interface EmployeeQueryService {

    Employee findByPin(String pin);

    Employee findByPinForUpdate(String pin, EmployeeType type);

}
