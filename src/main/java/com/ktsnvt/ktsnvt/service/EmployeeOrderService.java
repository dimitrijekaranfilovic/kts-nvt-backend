package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.OrderItem;

public interface EmployeeOrderService {

    void throwIfWaiterNotResponsible(String pin, Integer waiterId);

    void throwIfNotValidEmployeeType(Employee employee, OrderItem item);

}
