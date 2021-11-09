package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.Employee;

public interface OrderService {
    boolean hasAssignedActiveOrders(Employee employee);
}
